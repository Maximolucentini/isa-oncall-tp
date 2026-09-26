import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IIncidenteDeProveedor } from '../incidente-de-proveedor.model';
import { IncidenteDeProveedorService } from '../service/incidente-de-proveedor.service';

const incidenteDeProveedorResolve = (route: ActivatedRouteSnapshot): Observable<null | IIncidenteDeProveedor> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(IncidenteDeProveedorService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default incidenteDeProveedorResolve;
