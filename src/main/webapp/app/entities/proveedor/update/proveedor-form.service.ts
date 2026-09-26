import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProveedor, NewProveedor } from '../proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProveedor for edit and NewProveedorFormGroupInput for create.
 */
type ProveedorFormGroupInput = IProveedor | PartialWithRequiredKeyOf<NewProveedor>;

type ProveedorFormDefaults = Pick<NewProveedor, 'id' | 'activo'>;

type ProveedorFormGroupContent = {
  id: FormControl<IProveedor['id'] | NewProveedor['id']>;
  nombre: FormControl<IProveedor['nombre']>;
  tipo: FormControl<IProveedor['tipo']>;
  zonaHoraria: FormControl<IProveedor['zonaHoraria']>;
  telefonoContacto: FormControl<IProveedor['telefonoContacto']>;
  emailContacto: FormControl<IProveedor['emailContacto']>;
  urlSoporte: FormControl<IProveedor['urlSoporte']>;
  urlEstado: FormControl<IProveedor['urlEstado']>;
  cobertura: FormControl<IProveedor['cobertura']>;
  slaRespuestaMinutos: FormControl<IProveedor['slaRespuestaMinutos']>;
  activo: FormControl<IProveedor['activo']>;
};

export type ProveedorFormGroup = FormGroup<ProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProveedorFormService {
  createProveedorFormGroup(proveedor?: ProveedorFormGroupInput): ProveedorFormGroup {
    const proveedorRawValue = {
      ...this.getFormDefaults(),
      ...(proveedor ?? { id: null }),
    };

    return new FormGroup<ProveedorFormGroupContent>({
      id: new FormControl(
        { value: proveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(proveedorRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      tipo: new FormControl(proveedorRawValue.tipo, {
        validators: [Validators.required],
      }),
      zonaHoraria: new FormControl(proveedorRawValue.zonaHoraria, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      telefonoContacto: new FormControl(proveedorRawValue.telefonoContacto, {
        validators: [Validators.maxLength(40)],
      }),
      emailContacto: new FormControl(proveedorRawValue.emailContacto, {
        validators: [Validators.maxLength(120)],
      }),
      urlSoporte: new FormControl(proveedorRawValue.urlSoporte, {
        validators: [Validators.maxLength(500)],
      }),
      urlEstado: new FormControl(proveedorRawValue.urlEstado, {
        validators: [Validators.maxLength(500)],
      }),
      cobertura: new FormControl(proveedorRawValue.cobertura, {
        validators: [Validators.required],
      }),
      slaRespuestaMinutos: new FormControl(proveedorRawValue.slaRespuestaMinutos, {
        validators: [Validators.min(1), Validators.max(10080)],
      }),
      activo: new FormControl(proveedorRawValue.activo, {
        validators: [Validators.required],
      }),
    });
  }

  getProveedor(form: ProveedorFormGroup): IProveedor | NewProveedor {
    return form.getRawValue();
  }

  resetForm(form: ProveedorFormGroup, proveedor: ProveedorFormGroupInput): void {
    const proveedorRawValue = { ...this.getFormDefaults(), ...proveedor };
    form.reset({
      ...proveedorRawValue,
      id: { value: proveedorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProveedorFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
