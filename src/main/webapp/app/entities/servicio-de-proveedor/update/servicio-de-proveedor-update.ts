import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ServicioDeProveedorService } from '../service/servicio-de-proveedor.service';
import { IServicioDeProveedor } from '../servicio-de-proveedor.model';

import { ServicioDeProveedorFormGroup, ServicioDeProveedorFormService } from './servicio-de-proveedor-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-servicio-de-proveedor-update',
  templateUrl: './servicio-de-proveedor-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ServicioDeProveedorUpdate implements OnInit {
  readonly isSaving = signal(false);
  servicioDeProveedor: IServicioDeProveedor | null = null;

  proveedorsSharedCollection = signal<IProveedor[]>([]);
  serviciosSharedCollection = signal<IServicio[]>([]);

  protected servicioDeProveedorService = inject(ServicioDeProveedorService);
  protected servicioDeProveedorFormService = inject(ServicioDeProveedorFormService);
  protected proveedorService = inject(ProveedorService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServicioDeProveedorFormGroup = this.servicioDeProveedorFormService.createServicioDeProveedorFormGroup();

  compareProveedor = (o1: IProveedor | null, o2: IProveedor | null): boolean => this.proveedorService.compareProveedor(o1, o2);

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicioDeProveedor }) => {
      this.servicioDeProveedor = servicioDeProveedor;
      if (servicioDeProveedor) {
        this.updateForm(servicioDeProveedor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const servicioDeProveedor = this.servicioDeProveedorFormService.getServicioDeProveedor(this.editForm);
    if (servicioDeProveedor.id === null) {
      this.subscribeToSaveResponse(this.servicioDeProveedorService.create(servicioDeProveedor));
    } else {
      this.subscribeToSaveResponse(this.servicioDeProveedorService.update(servicioDeProveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServicioDeProveedor | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(servicioDeProveedor: IServicioDeProveedor): void {
    this.servicioDeProveedor = servicioDeProveedor;
    this.servicioDeProveedorFormService.resetForm(this.editForm, servicioDeProveedor);

    this.proveedorsSharedCollection.update(proveedors =>
      this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(proveedors, servicioDeProveedor.proveedor),
    );
    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, ...(servicioDeProveedor.servicioInternos ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proveedorService
      .query()
      .pipe(map((res: HttpResponse<IProveedor[]>) => res.body ?? []))
      .pipe(
        map((proveedors: IProveedor[]) =>
          this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(proveedors, this.servicioDeProveedor?.proveedor),
        ),
      )
      .subscribe((proveedors: IProveedor[]) => this.proveedorsSharedCollection.set(proveedors));

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(
            servicios,
            ...(this.servicioDeProveedor?.servicioInternos ?? []),
          ),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
