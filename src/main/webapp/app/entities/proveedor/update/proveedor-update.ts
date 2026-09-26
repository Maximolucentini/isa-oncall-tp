import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { CoberturaProveedor } from 'app/entities/enumerations/cobertura-proveedor.model';
import { TipoProveedor } from 'app/entities/enumerations/tipo-proveedor.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

import { ProveedorFormGroup, ProveedorFormService } from './proveedor-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProveedorUpdate implements OnInit {
  readonly isSaving = signal(false);
  proveedor: IProveedor | null = null;
  tipoProveedorValues = Object.keys(TipoProveedor);
  coberturaProveedorValues = Object.keys(CoberturaProveedor);

  protected proveedorService = inject(ProveedorService);
  protected proveedorFormService = inject(ProveedorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProveedorFormGroup = this.proveedorFormService.createProveedorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.proveedor = proveedor;
      if (proveedor) {
        this.updateForm(proveedor);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const proveedor = this.proveedorFormService.getProveedor(this.editForm);
    if (proveedor.id === null) {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProveedor | null>): void {
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

  protected updateForm(proveedor: IProveedor): void {
    this.proveedor = proveedor;
    this.proveedorFormService.resetForm(this.editForm, proveedor);
  }
}
