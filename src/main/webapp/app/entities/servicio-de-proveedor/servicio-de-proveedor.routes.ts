import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ServicioDeProveedorResolve from './route/servicio-de-proveedor-routing-resolve.service';

const servicioDeProveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/servicio-de-proveedor').then(m => m.ServicioDeProveedor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/servicio-de-proveedor-detail').then(m => m.ServicioDeProveedorDetail),
    resolve: {
      servicioDeProveedor: ServicioDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/servicio-de-proveedor-update').then(m => m.ServicioDeProveedorUpdate),
    resolve: {
      servicioDeProveedor: ServicioDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/servicio-de-proveedor-update').then(m => m.ServicioDeProveedorUpdate),
    resolve: {
      servicioDeProveedor: ServicioDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default servicioDeProveedorRoute;
