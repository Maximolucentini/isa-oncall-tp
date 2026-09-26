import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IServicioDeProveedor } from '../servicio-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-servicio-de-proveedor-detail',
  templateUrl: './servicio-de-proveedor-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink],
})
export class ServicioDeProveedorDetail {
  readonly servicioDeProveedor = input<IServicioDeProveedor | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
