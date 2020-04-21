package com.example.demo.service.onlinetest;

import com.example.demo.execute.JavaClassExecuter;
import com.example.demo.execute.StringSourceCompiler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class OnlineTestService {

    private static final int RUN_TIME_LIMITED = 15;

    private static final int N_THREAD = 150;

    private static final ExecutorService threadPool = new ThreadPoolExecutor(N_THREAD, N_THREAD, 60L, TimeUnit.SECONDS,
                                                                               new ArrayBlockingQueue<Runnable>(N_THREAD));

    private static final String WAIT_WARNING = "服务器繁忙，稍后提交";

    private static final String NO_OUTPUT = "没有输出值";

    /**
     *
     * @param code  代表代码
     * @param param 代表参数
     * @return  返回信息
     *
     * Diagnostic  收集程序的诊断信息,，即是否编译成功，编译失败是因为什么导致的失败
     */
    public Map<String, String> getCode(String code, String param){

        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();
        Map<String, String> map  = new HashMap<>();
        byte[] classBytes;
        try{
            classBytes = StringSourceCompiler.compile(code, compileCollector);
        }catch(IllegalArgumentException e) {
            map.put("runResult", "没有函数存在，请仔细检查程序");
            map.put("type", "error");
            return map;
        }
        /*编译出错打印信息*/
        if(classBytes == null){
            List<Diagnostic<? extends  JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorString = new StringBuilder();
            for(Diagnostic diagnostic : compileError){
/*                System.out.println("===================" + diagnostic.getCode());
                System.out.println("===================" + diagnostic.getEndPosition());
                System.out.println("===================" + diagnostic.getKind());
                System.out.println("===================" + diagnostic.getSource());
                System.out.println("===================" + diagnostic.toString());*/
                compileErrorString.append("compile error at: ");
                compileErrorString.append(diagnostic.getLineNumber());
                compileErrorString.append(".");
                compileErrorString.append(System.lineSeparator());
                compileErrorString.append("      " + diagnostic.toString());
            }
            String err = compileErrorString.toString().replaceAll(System.lineSeparator(), "<br/>");
            map.put("runResult", compileErrorString.toString());
            map.put("type", "error");
            return map;
        }

        /*编译通过*/
        Callable<String> runTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JavaClassExecuter.execute(classBytes, param);
            }
        };

        Future<String> res = null;
        try{
            res = threadPool.submit(runTask);
        }catch(RejectedExecutionException e){
            map.put("runResult", WAIT_WARNING);
            map.put("type", "warn");
            return map;
        }

        String runResult;
        try{
            runResult = res.get(RUN_TIME_LIMITED, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            runResult = "program interrupted";
        }catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = "Time Limit Exceeded";
        } finally {
            res.cancel(true);
        }
        runResult =  runResult != null ? runResult : NO_OUTPUT;
        runResult = runResult.replaceAll(System.lineSeparator(),"<br/>");
        map.put("runResult", runResult);
        map.put("type", "success");
        return map;
    }

    public String downloadcode(String code,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {

        String fileName = "test.java";
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(code);
        fileWriter.close();

        if (file.exists()) {

            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
