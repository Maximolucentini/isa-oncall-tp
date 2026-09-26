import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IContactoDeProveedor } from 'app/entities/contacto-de-proveedor/contacto-de-proveedor.model';
import { ContactoDeProveedorService } from 'app/entities/contacto-de-proveedor/service/contacto-de-proveedor.service';
import { NivelGuardia } from 'app/entities/enumerations/nivel-guardia.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TurnoDeProveedorService } from '../service/turno-de-proveedor.service';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

import { TurnoDeProveedorFormGroup, TurnoDeProveedorFormService } from './turno-de-proveedor-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-turno-de-proveedor-update',
  templateUrl: './turno-de-proveedor-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TurnoDeProveedorUpdate implements OnInit {
  readonly isSaving = signal(false);
  turnoDeProveedor: ITurnoDeProveedor | null = null;
  nivelGuardiaValues = Object.keys(NivelGuardia);

  contactoDeProveedorsSharedCollection = signal<IContactoDeProveedor[]>([]);

  protected turnoDeProveedorService = inject(TurnoDeProveedorService);
  protected turnoDeProveedorFormService = inject(TurnoDeProveedorFormService);
  protected contactoDeProveedorService = inject(ContactoDeProveedorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TurnoDeProveedorFormGroup = this.turnoDeProveedorFormService.createTurnoDeProveedorFormGroup();

  compareContactoDeProveedor = (o1: IContactoDeProveedor | null, o2: IContactoDeProveedor | null): boolean =>
    this.contactoDeProveedorService.compareContactoDeProveedor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turnoDeProveedor }) => {
      this.turnoDeProveedor = turnoDeProveedor;
      if (turnoDeProveedor) {
        this.updateForm(turnoDeProveedor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const turnoDeProveedor = this.turnoDeProveedorFormService.getTurnoDeProveedor(this.editForm);
    if (turnoDeProveedor.id === null) {
      this.subscribeToSaveResponse(this.turnoDeProveedorService.create(turnoDeProveedor));
    } else {
      this.subscribeToSaveResponse(this.turnoDeProveedorService.update(turnoDeProveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITurnoDeProveedor | null>): void {
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

  protected updateForm(turnoDeProveedor: ITurnoDeProveedor): void {
    this.turnoDeProveedor = turnoDeProveedor;
    this.turnoDeProveedorFormService.resetForm(this.editForm, turnoDeProveedor);

    this.contactoDeProveedorsSharedCollection.update(contactoDeProveedors =>
      this.contactoDeProveedorService.addContactoDeProveedorToCollectionIfMissing<IContactoDeProveedor>(
        contactoDeProveedors,
        turnoDeProveedor.contacto,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactoDeProveedorService
      .query()
      .pipe(map((res: HttpResponse<IContactoDeProveedor[]>) => res.body ?? []))
      .pipe(
        map((contactoDeProveedors: IContactoDeProveedor[]) =>
          this.contactoDeProveedorService.addContactoDeProveedorToCollectionIfMissing<IContactoDeProveedor>(
            contactoDeProveedors,
            this.turnoDeProveedor?.contacto,
          ),
        ),
      )
      .subscribe((contactoDeProveedors: IContactoDeProveedor[]) => this.contactoDeProveedorsSharedCollection.set(contactoDeProveedors));
  }
}
