import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { EstadoTicketProveedor } from 'app/entities/enumerations/estado-ticket-proveedor.model';
import { NivelGuardia } from 'app/entities/enumerations/nivel-guardia.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { ServicioDeProveedorService } from 'app/entities/servicio-de-proveedor/service/servicio-de-proveedor.service';
import { IServicioDeProveedor } from 'app/entities/servicio-de-proveedor/servicio-de-proveedor.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IIncidenteDeProveedor } from '../incidente-de-proveedor.model';
import { IncidenteDeProveedorService } from '../service/incidente-de-proveedor.service';

import { IncidenteDeProveedorFormGroup, IncidenteDeProveedorFormService } from './incidente-de-proveedor-form.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-incidente-de-proveedor-update',
  templateUrl: './incidente-de-proveedor-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class IncidenteDeProveedorUpdate implements OnInit {
  readonly isSaving = signal(false);
  incidenteDeProveedor: IIncidenteDeProveedor | null = null;
  estadoTicketProveedorValues = Object.keys(EstadoTicketProveedor);
  nivelGuardiaValues = Object.keys(NivelGuardia);

  incidentesSharedCollection = signal<IIncidente[]>([]);
  servicioDeProveedorsSharedCollection = signal<IServicioDeProveedor[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected incidenteDeProveedorService = inject(IncidenteDeProveedorService);
  protected incidenteDeProveedorFormService = inject(IncidenteDeProveedorFormService);
  protected incidenteService = inject(IncidenteService);
  protected servicioDeProveedorService = inject(ServicioDeProveedorService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IncidenteDeProveedorFormGroup = this.incidenteDeProveedorFormService.createIncidenteDeProveedorFormGroup();

  compareIncidente = (o1: IIncidente | null, o2: IIncidente | null): boolean => this.incidenteService.compareIncidente(o1, o2);

  compareServicioDeProveedor = (o1: IServicioDeProveedor | null, o2: IServicioDeProveedor | null): boolean =>
    this.servicioDeProveedorService.compareServicioDeProveedor(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incidenteDeProveedor }) => {
      this.incidenteDeProveedor = incidenteDeProveedor;
      if (incidenteDeProveedor) {
        this.updateForm(incidenteDeProveedor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const incidenteDeProveedor = this.incidenteDeProveedorFormService.getIncidenteDeProveedor(this.editForm);
    if (incidenteDeProveedor.id === null) {
      this.subscribeToSaveResponse(this.incidenteDeProveedorService.create(incidenteDeProveedor));
    } else {
      this.subscribeToSaveResponse(this.incidenteDeProveedorService.update(incidenteDeProveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IIncidenteDeProveedor | null>): void {
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

  protected updateForm(incidenteDeProveedor: IIncidenteDeProveedor): void {
    this.incidenteDeProveedor = incidenteDeProveedor;
    this.incidenteDeProveedorFormService.resetForm(this.editForm, incidenteDeProveedor);

    this.incidentesSharedCollection.update(incidentes =>
      this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, incidenteDeProveedor.incidente),
    );
    this.servicioDeProveedorsSharedCollection.update(servicioDeProveedors =>
      this.servicioDeProveedorService.addServicioDeProveedorToCollectionIfMissing<IServicioDeProveedor>(
        servicioDeProveedors,
        incidenteDeProveedor.servicioDeProveedor,
      ),
    );
    this.usersSharedCollection.update(users =>
      this.userService.addUserToCollectionIfMissing<IUser>(users, incidenteDeProveedor.abiertoPor),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.incidenteService
      .query()
      .pipe(map((res: HttpResponse<IIncidente[]>) => res.body ?? []))
      .pipe(
        map((incidentes: IIncidente[]) =>
          this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, this.incidenteDeProveedor?.incidente),
        ),
      )
      .subscribe((incidentes: IIncidente[]) => this.incidentesSharedCollection.set(incidentes));

    this.servicioDeProveedorService
      .query()
      .pipe(map((res: HttpResponse<IServicioDeProveedor[]>) => res.body ?? []))
      .pipe(
        map((servicioDeProveedors: IServicioDeProveedor[]) =>
          this.servicioDeProveedorService.addServicioDeProveedorToCollectionIfMissing<IServicioDeProveedor>(
            servicioDeProveedors,
            this.incidenteDeProveedor?.servicioDeProveedor,
          ),
        ),
      )
      .subscribe((servicioDeProveedors: IServicioDeProveedor[]) => this.servicioDeProveedorsSharedCollection.set(servicioDeProveedors));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.incidenteDeProveedor?.abiertoPor)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
