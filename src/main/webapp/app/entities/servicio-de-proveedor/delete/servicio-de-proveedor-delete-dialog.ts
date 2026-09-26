import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ServicioDeProveedorService } from '../service/servicio-de-proveedor.service';
import { IServicioDeProveedor } from '../servicio-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './servicio-de-proveedor-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ServicioDeProveedorDeleteDialog {
  servicioDeProveedor?: IServicioDeProveedor;

  protected readonly servicioDeProveedorService = inject(ServicioDeProveedorService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicioDeProveedorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
