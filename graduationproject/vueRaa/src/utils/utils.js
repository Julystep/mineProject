import {
  getRequest
} from "./api"

export const initMenu = (router, store) => {
  if (store.state.routes.length > 0) {
    return;
  }
  getRequest("/config/sysmenu").then(resp => {
    if (resp && resp.status == 200) {
      var fmtRoutes = formatRoutes(resp.data);
      router.addRoutes(fmtRoutes);
      store.commit('initMenu', fmtRoutes);
      store.dispatch('connect');
    }
  })
}

export const formatRoutes = (routes) => {
  let fmRoutes = [];
  routes.forEach(router => {
    let {
      path,
      component,
      name,
      children,
      meta
    } = router;
    if (children && children instanceof Array) {
      children = formatRoutes(children);
    }
    if (!path.startsWith("/sys")) {
      let fmRouter = {
        path: path,
        component(resolve) {
          if (component.startsWith("Home")) {
            require(['../components/' + component + '.vue'], resolve)
          } else if (component.startsWith("Su")) {
            require(['../components/teacher/' + component + '.vue'], resolve)
          } else if (component.startsWith("ScheckO")) {
            require(['../components/student/' + component + '.vue'], resolve)
          } else if (component.startsWith("CheckO")) {
            require(['../components/teacher/' + component + '.vue'], resolve)
          } else if (component.startsWith("Ch")) {
            require(['../components/teacher/' + component + '.vue'], resolve)
          } else if (component.startsWith("Sc")) {
            require(['../components/student/' + component + '.vue'], resolve)
          } else if (component.startsWith("Ad")) {
            require(['../components/root/' + component + '.vue'], resolve)
          } else if (component.startsWith("Ma")) {
            require(['../components/admin/' + component + '.vue'], resolve)
          }
        },
        name: name,
        children: children,
        meta: meta
      }
      fmRoutes.push(fmRouter);
    }
  })
  return fmRoutes;
}
