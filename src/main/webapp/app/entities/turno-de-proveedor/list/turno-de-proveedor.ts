import { HttpHeaders } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, WritableSignal, computed, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { TurnoDeProveedorDeleteDialog } from '../delete/turno-de-proveedor-delete-dialog';
import { TurnoDeProveedorService } from '../service/turno-de-proveedor.service';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-turno-de-proveedor',
  templateUrl: './turno-de-proveedor.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    FormatMediumDatetimePipe,
    InfiniteScrollDirective,
  ],
})
export class TurnoDeProveedor implements OnInit {
  subscription: Subscription | null = null;
  readonly turnoDeProveedors = signal<ITurnoDeProveedor[]>([]);

  sortState = sortStateSignal({});

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly links: WritableSignal<Record<string, undefined | Record<string, string | undefined>>> = signal({});
  readonly hasMorePage = computed(() => !!this.links().next);
  readonly isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  readonly router = inject(Router);
  protected readonly turnoDeProveedorService = inject(TurnoDeProveedorService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.turnoDeProveedorService.turnoDeProveedorsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.turnoDeProveedorService.turnoDeProveedorsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.turnoDeProveedors.update(turnoDeProveedors =>
        this.fillComponentAttributesFromResponseBody([...this.turnoDeProveedorService.turnoDeProveedors()], turnoDeProveedors),
      );
    });
  }

  trackId = (item: ITurnoDeProveedor): number => this.turnoDeProveedorService.getTurnoDeProveedorIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  reset(): void {
    this.turnoDeProveedors.set([]);
  }

  loadNextPage(): void {
    this.load();
  }

  delete(turnoDeProveedor: ITurnoDeProveedor): void {
    const modalRef = this.modalService.open(TurnoDeProveedorDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.turnoDeProveedor = turnoDeProveedor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected fillComponentAttributesFromResponseBody(data: ITurnoDeProveedor[], currentValue: ITurnoDeProveedor[]): ITurnoDeProveedor[] {
    const turnoDeProveedorsNew = [...currentValue];
    for (const d of data) {
      if (!turnoDeProveedorsNew.some(op => op.id === d.id)) {
        turnoDeProveedorsNew.push(d);
      }
    }
    return turnoDeProveedorsNew;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): void {
    const queryObject: any = {
      size: this.itemsPerPage(),
      eagerload: true,
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    this.turnoDeProveedorService.turnoDeProveedorsParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
