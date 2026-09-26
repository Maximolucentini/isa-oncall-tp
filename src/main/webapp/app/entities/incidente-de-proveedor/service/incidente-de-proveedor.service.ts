import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IIncidenteDeProveedor, NewIncidenteDeProveedor } from '../incidente-de-proveedor.model';

export type PartialUpdateIncidenteDeProveedor = Partial<IIncidenteDeProveedor> & Pick<IIncidenteDeProveedor, 'id'>;

type RestOf<T extends IIncidenteDeProveedor | NewIncidenteDeProveedor> = Omit<T, 'abiertoEn' | 'primeraRespuestaEn' | 'resueltoEn'> & {
  abiertoEn?: string | null;
  primeraRespuestaEn?: string | null;
  resueltoEn?: string | null;
};

export type RestIncidenteDeProveedor = RestOf<IIncidenteDeProveedor>;

export type NewRestIncidenteDeProveedor = RestOf<NewIncidenteDeProveedor>;

export type PartialUpdateRestIncidenteDeProveedor = RestOf<PartialUpdateIncidenteDeProveedor>;

@Injectable()
export class IncidenteDeProveedorsService {
  readonly incidenteDeProveedorsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly incidenteDeProveedorsResource = httpResource<RestIncidenteDeProveedor[]>(() => {
    const params = this.incidenteDeProveedorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of incidenteDeProveedor that have been fetched. It is updated when the incidenteDeProveedorsResource emits a new value.
   * In case of error while fetching the incidenteDeProveedors, the signal is set to an empty array.
   */
  readonly incidenteDeProveedors = computed(() =>
    (this.incidenteDeProveedorsResource.hasValue() ? this.incidenteDeProveedorsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/incidente-de-proveedors');

  protected convertValueFromServer(restIncidenteDeProveedor: RestIncidenteDeProveedor): IIncidenteDeProveedor {
    return {
      ...restIncidenteDeProveedor,
      abiertoEn: restIncidenteDeProveedor.abiertoEn ? dayjs(restIncidenteDeProveedor.abiertoEn) : undefined,
      primeraRespuestaEn: restIncidenteDeProveedor.primeraRespuestaEn ? dayjs(restIncidenteDeProveedor.primeraRespuestaEn) : undefined,
      resueltoEn: restIncidenteDeProveedor.resueltoEn ? dayjs(restIncidenteDeProveedor.resueltoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class IncidenteDeProveedorService extends IncidenteDeProveedorsService {
  protected readonly http = inject(HttpClient);

  create(incidenteDeProveedor: NewIncidenteDeProveedor): Observable<IIncidenteDeProveedor> {
    const copy = this.convertValueFromClient(incidenteDeProveedor);
    return this.http.post<RestIncidenteDeProveedor>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incidenteDeProveedor: IIncidenteDeProveedor): Observable<IIncidenteDeProveedor> {
    const copy = this.convertValueFromClient(incidenteDeProveedor);
    return this.http
      .put<RestIncidenteDeProveedor>(
        `${this.resourceUrl}/${encodeURIComponent(this.getIncidenteDeProveedorIdentifier(incidenteDeProveedor))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incidenteDeProveedor: PartialUpdateIncidenteDeProveedor): Observable<IIncidenteDeProveedor> {
    const copy = this.convertValueFromClient(incidenteDeProveedor);
    return this.http
      .patch<RestIncidenteDeProveedor>(
        `${this.resourceUrl}/${encodeURIComponent(this.getIncidenteDeProveedorIdentifier(incidenteDeProveedor))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IIncidenteDeProveedor> {
    return this.http
      .get<RestIncidenteDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IIncidenteDeProveedor[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncidenteDeProveedor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getIncidenteDeProveedorIdentifier(incidenteDeProveedor: Pick<IIncidenteDeProveedor, 'id'>): number {
    return incidenteDeProveedor.id;
  }

  compareIncidenteDeProveedor(o1: Pick<IIncidenteDeProveedor, 'id'> | null, o2: Pick<IIncidenteDeProveedor, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncidenteDeProveedorIdentifier(o1) === this.getIncidenteDeProveedorIdentifier(o2) : o1 === o2;
  }

  addIncidenteDeProveedorToCollectionIfMissing<Type extends Pick<IIncidenteDeProveedor, 'id'>>(
    incidenteDeProveedorCollection: Type[],
    ...incidenteDeProveedorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incidenteDeProveedors: Type[] = incidenteDeProveedorsToCheck.filter(isPresent);
    if (incidenteDeProveedors.length > 0) {
      const incidenteDeProveedorCollectionIdentifiers = incidenteDeProveedorCollection.map(incidenteDeProveedorItem =>
        this.getIncidenteDeProveedorIdentifier(incidenteDeProveedorItem),
      );
      const incidenteDeProveedorsToAdd = incidenteDeProveedors.filter(incidenteDeProveedorItem => {
        const incidenteDeProveedorIdentifier = this.getIncidenteDeProveedorIdentifier(incidenteDeProveedorItem);
        if (incidenteDeProveedorCollectionIdentifiers.includes(incidenteDeProveedorIdentifier)) {
          return false;
        }
        incidenteDeProveedorCollectionIdentifiers.push(incidenteDeProveedorIdentifier);
        return true;
      });
      return [...incidenteDeProveedorsToAdd, ...incidenteDeProveedorCollection];
    }
    return incidenteDeProveedorCollection;
  }

  protected convertValueFromClient<T extends IIncidenteDeProveedor | NewIncidenteDeProveedor | PartialUpdateIncidenteDeProveedor>(
    incidenteDeProveedor: T,
  ): RestOf<T> {
    return {
      ...incidenteDeProveedor,
      abiertoEn: incidenteDeProveedor.abiertoEn?.toJSON() ?? null,
      primeraRespuestaEn: incidenteDeProveedor.primeraRespuestaEn?.toJSON() ?? null,
      resueltoEn: incidenteDeProveedor.resueltoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestIncidenteDeProveedor): IIncidenteDeProveedor {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestIncidenteDeProveedor[]): IIncidenteDeProveedor[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
