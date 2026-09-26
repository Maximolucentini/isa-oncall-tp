import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IProveedor, NewProveedor } from '../proveedor.model';

export type PartialUpdateProveedor = Partial<IProveedor> & Pick<IProveedor, 'id'>;

@Injectable()
export class ProveedorsService {
  readonly proveedorsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly proveedorsResource = httpResource<IProveedor[]>(() => {
    const params = this.proveedorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of proveedor that have been fetched. It is updated when the proveedorsResource emits a new value.
   * In case of error while fetching the proveedors, the signal is set to an empty array.
   */
  readonly proveedors = computed(() => (this.proveedorsResource.hasValue() ? this.proveedorsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/proveedors');
}

@Injectable({ providedIn: 'root' })
export class ProveedorService extends ProveedorsService {
  protected readonly http = inject(HttpClient);

  create(proveedor: NewProveedor): Observable<IProveedor> {
    return this.http.post<IProveedor>(this.resourceUrl, proveedor);
  }

  update(proveedor: IProveedor): Observable<IProveedor> {
    return this.http.put<IProveedor>(`${this.resourceUrl}/${encodeURIComponent(this.getProveedorIdentifier(proveedor))}`, proveedor);
  }

  partialUpdate(proveedor: PartialUpdateProveedor): Observable<IProveedor> {
    return this.http.patch<IProveedor>(`${this.resourceUrl}/${encodeURIComponent(this.getProveedorIdentifier(proveedor))}`, proveedor);
  }

  find(id: number): Observable<IProveedor> {
    return this.http.get<IProveedor>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProveedor[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProveedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProveedorIdentifier(proveedor: Pick<IProveedor, 'id'>): number {
    return proveedor.id;
  }

  compareProveedor(o1: Pick<IProveedor, 'id'> | null, o2: Pick<IProveedor, 'id'> | null): boolean {
    return o1 && o2 ? this.getProveedorIdentifier(o1) === this.getProveedorIdentifier(o2) : o1 === o2;
  }

  addProveedorToCollectionIfMissing<Type extends Pick<IProveedor, 'id'>>(
    proveedorCollection: Type[],
    ...proveedorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const proveedors: Type[] = proveedorsToCheck.filter(isPresent);
    if (proveedors.length > 0) {
      const proveedorCollectionIdentifiers = proveedorCollection.map(proveedorItem => this.getProveedorIdentifier(proveedorItem));
      const proveedorsToAdd = proveedors.filter(proveedorItem => {
        const proveedorIdentifier = this.getProveedorIdentifier(proveedorItem);
        if (proveedorCollectionIdentifiers.includes(proveedorIdentifier)) {
          return false;
        }
        proveedorCollectionIdentifiers.push(proveedorIdentifier);
        return true;
      });
      return [...proveedorsToAdd, ...proveedorCollection];
    }
    return proveedorCollection;
  }
}
