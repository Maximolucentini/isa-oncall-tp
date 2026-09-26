import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import IncidenteDeProveedorResolve from './route/incidente-de-proveedor-routing-resolve.service';

const incidenteDeProveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/incidente-de-proveedor').then(m => m.IncidenteDeProveedor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/incidente-de-proveedor-detail').then(m => m.IncidenteDeProveedorDetail),
    resolve: {
      incidenteDeProveedor: IncidenteDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/incidente-de-proveedor-update').then(m => m.IncidenteDeProveedorUpdate),
    resolve: {
      incidenteDeProveedor: IncidenteDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/incidente-de-proveedor-update').then(m => m.IncidenteDeProveedorUpdate),
    resolve: {
      incidenteDeProveedor: IncidenteDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default incidenteDeProveedorRoute;
