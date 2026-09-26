import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IContactoDeProveedor } from '../contacto-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-contacto-de-proveedor-detail',
  templateUrl: './contacto-de-proveedor-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink],
})
export class ContactoDeProveedorDetail {
  readonly contactoDeProveedor = input<IContactoDeProveedor | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
