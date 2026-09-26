import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TurnoDeProveedorService } from '../service/turno-de-proveedor.service';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

const turnoDeProveedorResolve = (route: ActivatedRouteSnapshot): Observable<null | ITurnoDeProveedor> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TurnoDeProveedorService);
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

export default turnoDeProveedorResolve;
