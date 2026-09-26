import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IContactoDeProveedor } from '../contacto-de-proveedor.model';
import { ContactoDeProveedorService } from '../service/contacto-de-proveedor.service';

import { ContactoDeProveedorFormGroup, ContactoDeProveedorFormService } from './contacto-de-proveedor-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-contacto-de-proveedor-update',
  templateUrl: './contacto-de-proveedor-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ContactoDeProveedorUpdate implements OnInit {
  readonly isSaving = signal(false);
  contactoDeProveedor: IContactoDeProveedor | null = null;

  proveedorsSharedCollection = signal<IProveedor[]>([]);

  protected contactoDeProveedorService = inject(ContactoDeProveedorService);
  protected contactoDeProveedorFormService = inject(ContactoDeProveedorFormService);
  protected proveedorService = inject(ProveedorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContactoDeProveedorFormGroup = this.contactoDeProveedorFormService.createContactoDeProveedorFormGroup();

  compareProveedor = (o1: IProveedor | null, o2: IProveedor | null): boolean => this.proveedorService.compareProveedor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactoDeProveedor }) => {
      this.contactoDeProveedor = contactoDeProveedor;
      if (contactoDeProveedor) {
        this.updateForm(contactoDeProveedor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contactoDeProveedor = this.contactoDeProveedorFormService.getContactoDeProveedor(this.editForm);
    if (contactoDeProveedor.id === null) {
      this.subscribeToSaveResponse(this.contactoDeProveedorService.create(contactoDeProveedor));
    } else {
      this.subscribeToSaveResponse(this.contactoDeProveedorService.update(contactoDeProveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContactoDeProveedor | null>): void {
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

  protected updateForm(contactoDeProveedor: IContactoDeProveedor): void {
    this.contactoDeProveedor = contactoDeProveedor;
    this.contactoDeProveedorFormService.resetForm(this.editForm, contactoDeProveedor);

    this.proveedorsSharedCollection.update(proveedors =>
      this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(proveedors, contactoDeProveedor.proveedor),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proveedorService
      .query()
      .pipe(map((res: HttpResponse<IProveedor[]>) => res.body ?? []))
      .pipe(
        map((proveedors: IProveedor[]) =>
          this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(proveedors, this.contactoDeProveedor?.proveedor),
        ),
      )
      .subscribe((proveedors: IProveedor[]) => this.proveedorsSharedCollection.set(proveedors));
  }
}
