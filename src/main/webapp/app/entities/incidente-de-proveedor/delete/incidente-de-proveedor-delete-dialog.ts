import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IIncidenteDeProveedor } from '../incidente-de-proveedor.model';
import { IncidenteDeProveedorService } from '../service/incidente-de-proveedor.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './incidente-de-proveedor-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class IncidenteDeProveedorDeleteDialog {
  incidenteDeProveedor?: IIncidenteDeProveedor;

  protected readonly incidenteDeProveedorService = inject(IncidenteDeProveedorService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incidenteDeProveedorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
