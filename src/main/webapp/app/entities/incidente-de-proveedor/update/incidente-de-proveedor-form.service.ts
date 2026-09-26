import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIncidenteDeProveedor, NewIncidenteDeProveedor } from '../incidente-de-proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncidenteDeProveedor for edit and NewIncidenteDeProveedorFormGroupInput for create.
 */
type IncidenteDeProveedorFormGroupInput = IIncidenteDeProveedor | PartialWithRequiredKeyOf<NewIncidenteDeProveedor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIncidenteDeProveedor | NewIncidenteDeProveedor> = Omit<T, 'abiertoEn' | 'primeraRespuestaEn' | 'resueltoEn'> & {
  abiertoEn?: string | null;
  primeraRespuestaEn?: string | null;
  resueltoEn?: string | null;
};

type IncidenteDeProveedorFormRawValue = FormValueOf<IIncidenteDeProveedor>;

type NewIncidenteDeProveedorFormRawValue = FormValueOf<NewIncidenteDeProveedor>;

type IncidenteDeProveedorFormDefaults = Pick<
  NewIncidenteDeProveedor,
  'id' | 'abiertoEn' | 'primeraRespuestaEn' | 'resueltoEn' | 'cumplioSla' | 'huboCobertura'
>;

type IncidenteDeProveedorFormGroupContent = {
  id: FormControl<IncidenteDeProveedorFormRawValue['id'] | NewIncidenteDeProveedor['id']>;
  ticketExterno: FormControl<IncidenteDeProveedorFormRawValue['ticketExterno']>;
  estado: FormControl<IncidenteDeProveedorFormRawValue['estado']>;
  abiertoEn: FormControl<IncidenteDeProveedorFormRawValue['abiertoEn']>;
  primeraRespuestaEn: FormControl<IncidenteDeProveedorFormRawValue['primeraRespuestaEn']>;
  resueltoEn: FormControl<IncidenteDeProveedorFormRawValue['resueltoEn']>;
  cumplioSla: FormControl<IncidenteDeProveedorFormRawValue['cumplioSla']>;
  motivoRechazo: FormControl<IncidenteDeProveedorFormRawValue['motivoRechazo']>;
  responsableResuelto: FormControl<IncidenteDeProveedorFormRawValue['responsableResuelto']>;
  nivelResuelto: FormControl<IncidenteDeProveedorFormRawValue['nivelResuelto']>;
  huboCobertura: FormControl<IncidenteDeProveedorFormRawValue['huboCobertura']>;
  notas: FormControl<IncidenteDeProveedorFormRawValue['notas']>;
  incidente: FormControl<IncidenteDeProveedorFormRawValue['incidente']>;
  servicioDeProveedor: FormControl<IncidenteDeProveedorFormRawValue['servicioDeProveedor']>;
  abiertoPor: FormControl<IncidenteDeProveedorFormRawValue['abiertoPor']>;
};

export type IncidenteDeProveedorFormGroup = FormGroup<IncidenteDeProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncidenteDeProveedorFormService {
  createIncidenteDeProveedorFormGroup(incidenteDeProveedor?: IncidenteDeProveedorFormGroupInput): IncidenteDeProveedorFormGroup {
    const incidenteDeProveedorRawValue = this.convertIncidenteDeProveedorToIncidenteDeProveedorRawValue({
      ...this.getFormDefaults(),
      ...(incidenteDeProveedor ?? { id: null }),
    });

    return new FormGroup<IncidenteDeProveedorFormGroupContent>({
      id: new FormControl(
        { value: incidenteDeProveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ticketExterno: new FormControl(incidenteDeProveedorRawValue.ticketExterno, {
        validators: [Validators.maxLength(100)],
      }),
      estado: new FormControl(incidenteDeProveedorRawValue.estado, {
        validators: [Validators.required],
      }),
      abiertoEn: new FormControl(incidenteDeProveedorRawValue.abiertoEn, {
        validators: [Validators.required],
      }),
      primeraRespuestaEn: new FormControl(incidenteDeProveedorRawValue.primeraRespuestaEn),
      resueltoEn: new FormControl(incidenteDeProveedorRawValue.resueltoEn),
      cumplioSla: new FormControl(incidenteDeProveedorRawValue.cumplioSla),
      motivoRechazo: new FormControl(incidenteDeProveedorRawValue.motivoRechazo, {
        validators: [Validators.maxLength(1000)],
      }),
      responsableResuelto: new FormControl(incidenteDeProveedorRawValue.responsableResuelto, {
        validators: [Validators.maxLength(100)],
      }),
      nivelResuelto: new FormControl(incidenteDeProveedorRawValue.nivelResuelto),
      huboCobertura: new FormControl(incidenteDeProveedorRawValue.huboCobertura),
      notas: new FormControl(incidenteDeProveedorRawValue.notas, {
        validators: [Validators.maxLength(4000)],
      }),
      incidente: new FormControl(incidenteDeProveedorRawValue.incidente),
      servicioDeProveedor: new FormControl(incidenteDeProveedorRawValue.servicioDeProveedor, {
        validators: [Validators.required],
      }),
      abiertoPor: new FormControl(incidenteDeProveedorRawValue.abiertoPor),
    });
  }

  getIncidenteDeProveedor(form: IncidenteDeProveedorFormGroup): IIncidenteDeProveedor | NewIncidenteDeProveedor {
    return this.convertIncidenteDeProveedorRawValueToIncidenteDeProveedor(form.getRawValue());
  }

  resetForm(form: IncidenteDeProveedorFormGroup, incidenteDeProveedor: IncidenteDeProveedorFormGroupInput): void {
    const incidenteDeProveedorRawValue = this.convertIncidenteDeProveedorToIncidenteDeProveedorRawValue({
      ...this.getFormDefaults(),
      ...incidenteDeProveedor,
    });
    form.reset({
      ...incidenteDeProveedorRawValue,
      id: { value: incidenteDeProveedorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): IncidenteDeProveedorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      abiertoEn: currentTime,
      primeraRespuestaEn: currentTime,
      resueltoEn: currentTime,
      cumplioSla: false,
      huboCobertura: false,
    };
  }

  private convertIncidenteDeProveedorRawValueToIncidenteDeProveedor(
    rawIncidenteDeProveedor: IncidenteDeProveedorFormRawValue | NewIncidenteDeProveedorFormRawValue,
  ): IIncidenteDeProveedor | NewIncidenteDeProveedor {
    return {
      ...rawIncidenteDeProveedor,
      abiertoEn: dayjs(rawIncidenteDeProveedor.abiertoEn, DATE_TIME_FORMAT),
      primeraRespuestaEn: dayjs(rawIncidenteDeProveedor.primeraRespuestaEn, DATE_TIME_FORMAT),
      resueltoEn: dayjs(rawIncidenteDeProveedor.resueltoEn, DATE_TIME_FORMAT),
    };
  }

  private convertIncidenteDeProveedorToIncidenteDeProveedorRawValue(
    incidenteDeProveedor: IIncidenteDeProveedor | (Partial<NewIncidenteDeProveedor> & IncidenteDeProveedorFormDefaults),
  ): IncidenteDeProveedorFormRawValue | PartialWithRequiredKeyOf<NewIncidenteDeProveedorFormRawValue> {
    return {
      ...incidenteDeProveedor,
      abiertoEn: incidenteDeProveedor.abiertoEn ? incidenteDeProveedor.abiertoEn.format(DATE_TIME_FORMAT) : undefined,
      primeraRespuestaEn: incidenteDeProveedor.primeraRespuestaEn
        ? incidenteDeProveedor.primeraRespuestaEn.format(DATE_TIME_FORMAT)
        : undefined,
      resueltoEn: incidenteDeProveedor.resueltoEn ? incidenteDeProveedor.resueltoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
