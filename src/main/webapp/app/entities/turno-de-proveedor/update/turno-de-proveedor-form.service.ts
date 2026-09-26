import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITurnoDeProveedor, NewTurnoDeProveedor } from '../turno-de-proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITurnoDeProveedor for edit and NewTurnoDeProveedorFormGroupInput for create.
 */
type TurnoDeProveedorFormGroupInput = ITurnoDeProveedor | PartialWithRequiredKeyOf<NewTurnoDeProveedor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITurnoDeProveedor | NewTurnoDeProveedor> = Omit<T, 'desde' | 'hasta'> & {
  desde?: string | null;
  hasta?: string | null;
};

type TurnoDeProveedorFormRawValue = FormValueOf<ITurnoDeProveedor>;

type NewTurnoDeProveedorFormRawValue = FormValueOf<NewTurnoDeProveedor>;

type TurnoDeProveedorFormDefaults = Pick<NewTurnoDeProveedor, 'id' | 'desde' | 'hasta' | 'esReemplazo'>;

type TurnoDeProveedorFormGroupContent = {
  id: FormControl<TurnoDeProveedorFormRawValue['id'] | NewTurnoDeProveedor['id']>;
  desde: FormControl<TurnoDeProveedorFormRawValue['desde']>;
  hasta: FormControl<TurnoDeProveedorFormRawValue['hasta']>;
  nivel: FormControl<TurnoDeProveedorFormRawValue['nivel']>;
  esReemplazo: FormControl<TurnoDeProveedorFormRawValue['esReemplazo']>;
  nota: FormControl<TurnoDeProveedorFormRawValue['nota']>;
  contacto: FormControl<TurnoDeProveedorFormRawValue['contacto']>;
};

export type TurnoDeProveedorFormGroup = FormGroup<TurnoDeProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TurnoDeProveedorFormService {
  createTurnoDeProveedorFormGroup(turnoDeProveedor?: TurnoDeProveedorFormGroupInput): TurnoDeProveedorFormGroup {
    const turnoDeProveedorRawValue = this.convertTurnoDeProveedorToTurnoDeProveedorRawValue({
      ...this.getFormDefaults(),
      ...(turnoDeProveedor ?? { id: null }),
    });

    return new FormGroup<TurnoDeProveedorFormGroupContent>({
      id: new FormControl(
        { value: turnoDeProveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      desde: new FormControl(turnoDeProveedorRawValue.desde, {
        validators: [Validators.required],
      }),
      hasta: new FormControl(turnoDeProveedorRawValue.hasta, {
        validators: [Validators.required],
      }),
      nivel: new FormControl(turnoDeProveedorRawValue.nivel, {
        validators: [Validators.required],
      }),
      esReemplazo: new FormControl(turnoDeProveedorRawValue.esReemplazo, {
        validators: [Validators.required],
      }),
      nota: new FormControl(turnoDeProveedorRawValue.nota, {
        validators: [Validators.maxLength(500)],
      }),
      contacto: new FormControl(turnoDeProveedorRawValue.contacto, {
        validators: [Validators.required],
      }),
    });
  }

  getTurnoDeProveedor(form: TurnoDeProveedorFormGroup): ITurnoDeProveedor | NewTurnoDeProveedor {
    return this.convertTurnoDeProveedorRawValueToTurnoDeProveedor(form.getRawValue());
  }

  resetForm(form: TurnoDeProveedorFormGroup, turnoDeProveedor: TurnoDeProveedorFormGroupInput): void {
    const turnoDeProveedorRawValue = this.convertTurnoDeProveedorToTurnoDeProveedorRawValue({
      ...this.getFormDefaults(),
      ...turnoDeProveedor,
    });
    form.reset({
      ...turnoDeProveedorRawValue,
      id: { value: turnoDeProveedorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TurnoDeProveedorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      desde: currentTime,
      hasta: currentTime,
      esReemplazo: false,
    };
  }

  private convertTurnoDeProveedorRawValueToTurnoDeProveedor(
    rawTurnoDeProveedor: TurnoDeProveedorFormRawValue | NewTurnoDeProveedorFormRawValue,
  ): ITurnoDeProveedor | NewTurnoDeProveedor {
    return {
      ...rawTurnoDeProveedor,
      desde: dayjs(rawTurnoDeProveedor.desde, DATE_TIME_FORMAT),
      hasta: dayjs(rawTurnoDeProveedor.hasta, DATE_TIME_FORMAT),
    };
  }

  private convertTurnoDeProveedorToTurnoDeProveedorRawValue(
    turnoDeProveedor: ITurnoDeProveedor | (Partial<NewTurnoDeProveedor> & TurnoDeProveedorFormDefaults),
  ): TurnoDeProveedorFormRawValue | PartialWithRequiredKeyOf<NewTurnoDeProveedorFormRawValue> {
    return {
      ...turnoDeProveedor,
      desde: turnoDeProveedor.desde ? turnoDeProveedor.desde.format(DATE_TIME_FORMAT) : undefined,
      hasta: turnoDeProveedor.hasta ? turnoDeProveedor.hasta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
