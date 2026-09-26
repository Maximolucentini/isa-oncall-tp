import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IContactoDeProveedor, NewContactoDeProveedor } from '../contacto-de-proveedor.model';

export type PartialUpdateContactoDeProveedor = Partial<IContactoDeProveedor> & Pick<IContactoDeProveedor, 'id'>;

@Injectable()
export class ContactoDeProveedorsService {
  readonly contactoDeProveedorsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly contactoDeProveedorsResource = httpResource<IContactoDeProveedor[]>(() => {
    const params = this.contactoDeProveedorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contactoDeProveedor that have been fetched. It is updated when the contactoDeProveedorsResource emits a new value.
   * In case of error while fetching the contactoDeProveedors, the signal is set to an empty array.
   */
  readonly contactoDeProveedors = computed(() =>
    this.contactoDeProveedorsResource.hasValue() ? this.contactoDeProveedorsResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/contacto-de-proveedors');
}

@Injectable({ providedIn: 'root' })
export class ContactoDeProveedorService extends ContactoDeProveedorsService {
  protected readonly http = inject(HttpClient);

  create(contactoDeProveedor: NewContactoDeProveedor): Observable<IContactoDeProveedor> {
    return this.http.post<IContactoDeProveedor>(this.resourceUrl, contactoDeProveedor);
  }

  update(contactoDeProveedor: IContactoDeProveedor): Observable<IContactoDeProveedor> {
    return this.http.put<IContactoDeProveedor>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactoDeProveedorIdentifier(contactoDeProveedor))}`,
      contactoDeProveedor,
    );
  }

  partialUpdate(contactoDeProveedor: PartialUpdateContactoDeProveedor): Observable<IContactoDeProveedor> {
    return this.http.patch<IContactoDeProveedor>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactoDeProveedorIdentifier(contactoDeProveedor))}`,
      contactoDeProveedor,
    );
  }

  find(id: number): Observable<IContactoDeProveedor> {
    return this.http.get<IContactoDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IContactoDeProveedor[]>> {
    const options = createRequestOption(req);
    return this.http.get<IContactoDeProveedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContactoDeProveedorIdentifier(contactoDeProveedor: Pick<IContactoDeProveedor, 'id'>): number {
    return contactoDeProveedor.id;
  }

  compareContactoDeProveedor(o1: Pick<IContactoDeProveedor, 'id'> | null, o2: Pick<IContactoDeProveedor, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactoDeProveedorIdentifier(o1) === this.getContactoDeProveedorIdentifier(o2) : o1 === o2;
  }

  addContactoDeProveedorToCollectionIfMissing<Type extends Pick<IContactoDeProveedor, 'id'>>(
    contactoDeProveedorCollection: Type[],
    ...contactoDeProveedorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactoDeProveedors: Type[] = contactoDeProveedorsToCheck.filter(isPresent);
    if (contactoDeProveedors.length > 0) {
      const contactoDeProveedorCollectionIdentifiers = contactoDeProveedorCollection.map(contactoDeProveedorItem =>
        this.getContactoDeProveedorIdentifier(contactoDeProveedorItem),
      );
      const contactoDeProveedorsToAdd = contactoDeProveedors.filter(contactoDeProveedorItem => {
        const contactoDeProveedorIdentifier = this.getContactoDeProveedorIdentifier(contactoDeProveedorItem);
        if (contactoDeProveedorCollectionIdentifiers.includes(contactoDeProveedorIdentifier)) {
          return false;
        }
        contactoDeProveedorCollectionIdentifiers.push(contactoDeProveedorIdentifier);
        return true;
      });
      return [...contactoDeProveedorsToAdd, ...contactoDeProveedorCollection];
    }
    return contactoDeProveedorCollection;
  }
}
