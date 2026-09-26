import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-turno-de-proveedor-detail',
  templateUrl: './turno-de-proveedor-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink, FormatMediumDatetimePipe],
})
export class TurnoDeProveedorDetail {
  readonly turnoDeProveedor = input<ITurnoDeProveedor | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
