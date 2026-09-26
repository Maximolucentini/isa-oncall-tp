import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServicioDeProveedor, NewServicioDeProveedor } from '../servicio-de-proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServicioDeProveedor for edit and NewServicioDeProveedorFormGroupInput for create.
 */
type ServicioDeProveedorFormGroupInput = IServicioDeProveedor | PartialWithRequiredKeyOf<NewServicioDeProveedor>;

type ServicioDeProveedorFormDefaults = Pick<NewServicioDeProveedor, 'id' | 'activo' | 'servicioInternos'>;

type ServicioDeProveedorFormGroupContent = {
  id: FormControl<IServicioDeProveedor['id'] | NewServicioDeProveedor['id']>;
  nombre: FormControl<IServicioDeProveedor['nombre']>;
  identificadorExterno: FormControl<IServicioDeProveedor['identificadorExterno']>;
  descripcion: FormControl<IServicioDeProveedor['descripcion']>;
  slaRespuestaMinutos: FormControl<IServicioDeProveedor['slaRespuestaMinutos']>;
  activo: FormControl<IServicioDeProveedor['activo']>;
  proveedor: FormControl<IServicioDeProveedor['proveedor']>;
  servicioInternos: FormControl<IServicioDeProveedor['servicioInternos']>;
};

export type ServicioDeProveedorFormGroup = FormGroup<ServicioDeProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicioDeProveedorFormService {
  createServicioDeProveedorFormGroup(servicioDeProveedor?: ServicioDeProveedorFormGroupInput): ServicioDeProveedorFormGroup {
    const servicioDeProveedorRawValue = {
      ...this.getFormDefaults(),
      ...(servicioDeProveedor ?? { id: null }),
    };

    return new FormGroup<ServicioDeProveedorFormGroupContent>({
      id: new FormControl(
        { value: servicioDeProveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(servicioDeProveedorRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      identificadorExterno: new FormControl(servicioDeProveedorRawValue.identificadorExterno, {
        validators: [Validators.maxLength(200)],
      }),
      descripcion: new FormControl(servicioDeProveedorRawValue.descripcion, {
        validators: [Validators.maxLength(500)],
      }),
      slaRespuestaMinutos: new FormControl(servicioDeProveedorRawValue.slaRespuestaMinutos, {
        validators: [Validators.min(1), Validators.max(10080)],
      }),
      activo: new FormControl(servicioDeProveedorRawValue.activo, {
        validators: [Validators.required],
      }),
      proveedor: new FormControl(servicioDeProveedorRawValue.proveedor, {
        validators: [Validators.required],
      }),
      servicioInternos: new FormControl(servicioDeProveedorRawValue.servicioInternos ?? []),
    });
  }

  getServicioDeProveedor(form: ServicioDeProveedorFormGroup): IServicioDeProveedor | NewServicioDeProveedor {
    return form.getRawValue();
  }

  resetForm(form: ServicioDeProveedorFormGroup, servicioDeProveedor: ServicioDeProveedorFormGroupInput): void {
    const servicioDeProveedorRawValue = { ...this.getFormDefaults(), ...servicioDeProveedor };
    form.reset({
      ...servicioDeProveedorRawValue,
      id: { value: servicioDeProveedorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServicioDeProveedorFormDefaults {
    return {
      id: null,
      activo: false,
      servicioInternos: [],
    };
  }
}
