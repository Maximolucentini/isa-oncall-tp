import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITurnoDeProveedor, NewTurnoDeProveedor } from '../turno-de-proveedor.model';

export type PartialUpdateTurnoDeProveedor = Partial<ITurnoDeProveedor> & Pick<ITurnoDeProveedor, 'id'>;

type RestOf<T extends ITurnoDeProveedor | NewTurnoDeProveedor> = Omit<T, 'desde' | 'hasta'> & {
  desde?: string | null;
  hasta?: string | null;
};

export type RestTurnoDeProveedor = RestOf<ITurnoDeProveedor>;

export type NewRestTurnoDeProveedor = RestOf<NewTurnoDeProveedor>;

export type PartialUpdateRestTurnoDeProveedor = RestOf<PartialUpdateTurnoDeProveedor>;

@Injectable()
export class TurnoDeProveedorsService {
  readonly turnoDeProveedorsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly turnoDeProveedorsResource = httpResource<RestTurnoDeProveedor[]>(() => {
    const params = this.turnoDeProveedorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of turnoDeProveedor that have been fetched. It is updated when the turnoDeProveedorsResource emits a new value.
   * In case of error while fetching the turnoDeProveedors, the signal is set to an empty array.
   */
  readonly turnoDeProveedors = computed(() =>
    (this.turnoDeProveedorsResource.hasValue() ? this.turnoDeProveedorsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/turno-de-proveedors');

  protected convertValueFromServer(restTurnoDeProveedor: RestTurnoDeProveedor): ITurnoDeProveedor {
    return {
      ...restTurnoDeProveedor,
      desde: restTurnoDeProveedor.desde ? dayjs(restTurnoDeProveedor.desde) : undefined,
      hasta: restTurnoDeProveedor.hasta ? dayjs(restTurnoDeProveedor.hasta) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class TurnoDeProveedorService extends TurnoDeProveedorsService {
  protected readonly http = inject(HttpClient);

  create(turnoDeProveedor: NewTurnoDeProveedor): Observable<ITurnoDeProveedor> {
    const copy = this.convertValueFromClient(turnoDeProveedor);
    return this.http.post<RestTurnoDeProveedor>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(turnoDeProveedor: ITurnoDeProveedor): Observable<ITurnoDeProveedor> {
    const copy = this.convertValueFromClient(turnoDeProveedor);
    return this.http
      .put<RestTurnoDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(this.getTurnoDeProveedorIdentifier(turnoDeProveedor))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(turnoDeProveedor: PartialUpdateTurnoDeProveedor): Observable<ITurnoDeProveedor> {
    const copy = this.convertValueFromClient(turnoDeProveedor);
    return this.http
      .patch<RestTurnoDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(this.getTurnoDeProveedorIdentifier(turnoDeProveedor))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ITurnoDeProveedor> {
    return this.http
      .get<RestTurnoDeProveedor>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ITurnoDeProveedor[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTurnoDeProveedor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTurnoDeProveedorIdentifier(turnoDeProveedor: Pick<ITurnoDeProveedor, 'id'>): number {
    return turnoDeProveedor.id;
  }

  compareTurnoDeProveedor(o1: Pick<ITurnoDeProveedor, 'id'> | null, o2: Pick<ITurnoDeProveedor, 'id'> | null): boolean {
    return o1 && o2 ? this.getTurnoDeProveedorIdentifier(o1) === this.getTurnoDeProveedorIdentifier(o2) : o1 === o2;
  }

  addTurnoDeProveedorToCollectionIfMissing<Type extends Pick<ITurnoDeProveedor, 'id'>>(
    turnoDeProveedorCollection: Type[],
    ...turnoDeProveedorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const turnoDeProveedors: Type[] = turnoDeProveedorsToCheck.filter(isPresent);
    if (turnoDeProveedors.length > 0) {
      const turnoDeProveedorCollectionIdentifiers = turnoDeProveedorCollection.map(turnoDeProveedorItem =>
        this.getTurnoDeProveedorIdentifier(turnoDeProveedorItem),
      );
      const turnoDeProveedorsToAdd = turnoDeProveedors.filter(turnoDeProveedorItem => {
        const turnoDeProveedorIdentifier = this.getTurnoDeProveedorIdentifier(turnoDeProveedorItem);
        if (turnoDeProveedorCollectionIdentifiers.includes(turnoDeProveedorIdentifier)) {
          return false;
        }
        turnoDeProveedorCollectionIdentifiers.push(turnoDeProveedorIdentifier);
        return true;
      });
      return [...turnoDeProveedorsToAdd, ...turnoDeProveedorCollection];
    }
    return turnoDeProveedorCollection;
  }

  protected convertValueFromClient<T extends ITurnoDeProveedor | NewTurnoDeProveedor | PartialUpdateTurnoDeProveedor>(
    turnoDeProveedor: T,
  ): RestOf<T> {
    return {
      ...turnoDeProveedor,
      desde: turnoDeProveedor.desde?.toJSON() ?? null,
      hasta: turnoDeProveedor.hasta?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestTurnoDeProveedor): ITurnoDeProveedor {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestTurnoDeProveedor[]): ITurnoDeProveedor[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
