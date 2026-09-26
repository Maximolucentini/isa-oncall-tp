import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TurnoDeProveedorService } from '../service/turno-de-proveedor.service';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './turno-de-proveedor-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class TurnoDeProveedorDeleteDialog {
  turnoDeProveedor?: ITurnoDeProveedor;

  protected readonly turnoDeProveedorService = inject(TurnoDeProveedorService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.turnoDeProveedorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
