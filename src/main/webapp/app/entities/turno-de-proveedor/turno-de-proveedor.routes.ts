import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TurnoDeProveedorResolve from './route/turno-de-proveedor-routing-resolve.service';

const turnoDeProveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/turno-de-proveedor').then(m => m.TurnoDeProveedor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/turno-de-proveedor-detail').then(m => m.TurnoDeProveedorDetail),
    resolve: {
      turnoDeProveedor: TurnoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/turno-de-proveedor-update').then(m => m.TurnoDeProveedorUpdate),
    resolve: {
      turnoDeProveedor: TurnoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/turno-de-proveedor-update').then(m => m.TurnoDeProveedorUpdate),
    resolve: {
      turnoDeProveedor: TurnoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default turnoDeProveedorRoute;
