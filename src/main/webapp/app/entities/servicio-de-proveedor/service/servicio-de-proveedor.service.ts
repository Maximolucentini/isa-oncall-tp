import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IServicioDeProveedor, NewServicioDeProveedor } from '../servicio-de-proveedor.model';

export type PartialUpdateServicioDeProveedor = Partial<IServicioDeProveedor> & Pick<IServicioDeProveedor, 'id'>;

@Injectable()
export class ServicioDeProveedorsService {
  readonly servicioDeProveedorsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly servicioDeProveedorsResource = httpResource<IServicioDeProveedor[]>(() => {
    const params = this.servicioDeProveedorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of servicioDeProveedor that have been fetched. It is updated when the servicioDeProveedorsResource emits a new value.
   * In case of error while fetching the servicioDeProveedors, the signal is set to an empty array.
   */
  readonly servicioDeProveedors = computed(() =>
    this.servicioDeProveedorsResource.hasValue() ? this.servicioDeProveedorsResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/servicio-de-proveedors');
}

@Injectable({ providedIn: 'root' })
export class ServicioDeProveedorService extends ServicioDeProveedorsService {
  protected readonly http = inject(HttpClient);

  create(servicioDeProveedor: NewServicioDeProveedor): Observable<IServicioDeProveedor> {
    return this.http.post<IServicioDeProveedor>(this.resourceUrl, servicioDeProveedor);
  }

  update(servicioDeProveedor: IServicioDeProveedor): Observable<IServicioDeProveedor> {
    return this.http.put<IServicioDeProveedor>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioDeProveedorIdentifier(servicioDeProveedor))}`,
      servicioDeProveedor,
    );
  }

  partialUpdate(servicioDeProveedor: PartialUpdateServicioDeProveedor): Observable<IServicioDeProveedor> {
    return this.http.patch<IServicioDeProveedor>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioDeProveedorIdentifier(servicioDeProveedor))}`,
      servicioDeProveedor,
    );
  }

  find(id: number): Observable<IServicioDeProveedor> {
    return this.http.get<IServicioDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IServicioDeProveedor[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServicioDeProveedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServicioDeProveedorIdentifier(servicioDeProveedor: Pick<IServicioDeProveedor, 'id'>): number {
    return servicioDeProveedor.id;
  }

  compareServicioDeProveedor(o1: Pick<IServicioDeProveedor, 'id'> | null, o2: Pick<IServicioDeProveedor, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicioDeProveedorIdentifier(o1) === this.getServicioDeProveedorIdentifier(o2) : o1 === o2;
  }

  addServicioDeProveedorToCollectionIfMissing<Type extends Pick<IServicioDeProveedor, 'id'>>(
    servicioDeProveedorCollection: Type[],
    ...servicioDeProveedorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicioDeProveedors: Type[] = servicioDeProveedorsToCheck.filter(isPresent);
    if (servicioDeProveedors.length > 0) {
      const servicioDeProveedorCollectionIdentifiers = servicioDeProveedorCollection.map(servicioDeProveedorItem =>
        this.getServicioDeProveedorIdentifier(servicioDeProveedorItem),
      );
      const servicioDeProveedorsToAdd = servicioDeProveedors.filter(servicioDeProveedorItem => {
        const servicioDeProveedorIdentifier = this.getServicioDeProveedorIdentifier(servicioDeProveedorItem);
        if (servicioDeProveedorCollectionIdentifiers.includes(servicioDeProveedorIdentifier)) {
          return false;
        }
        servicioDeProveedorCollectionIdentifiers.push(servicioDeProveedorIdentifier);
        return true;
      });
      return [...servicioDeProveedorsToAdd, ...servicioDeProveedorCollection];
    }
    return servicioDeProveedorCollection;
  }
}
