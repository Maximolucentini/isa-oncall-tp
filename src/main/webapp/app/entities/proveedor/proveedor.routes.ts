import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ProveedorResolve from './route/proveedor-routing-resolve.service';

const proveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/proveedor').then(m => m.Proveedor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/proveedor-detail').then(m => m.ProveedorDetail),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/proveedor-update').then(m => m.ProveedorUpdate),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/proveedor-update').then(m => m.ProveedorUpdate),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default proveedorRoute;
