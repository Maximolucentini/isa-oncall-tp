import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ContactoDeProveedorResolve from './route/contacto-de-proveedor-routing-resolve.service';

const contactoDeProveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contacto-de-proveedor').then(m => m.ContactoDeProveedor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contacto-de-proveedor-detail').then(m => m.ContactoDeProveedorDetail),
    resolve: {
      contactoDeProveedor: ContactoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contacto-de-proveedor-update').then(m => m.ContactoDeProveedorUpdate),
    resolve: {
      contactoDeProveedor: ContactoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contacto-de-proveedor-update').then(m => m.ContactoDeProveedorUpdate),
    resolve: {
      contactoDeProveedor: ContactoDeProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contactoDeProveedorRoute;
