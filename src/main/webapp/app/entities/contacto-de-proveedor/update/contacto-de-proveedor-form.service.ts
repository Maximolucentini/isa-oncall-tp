import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContactoDeProveedor, NewContactoDeProveedor } from '../contacto-de-proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactoDeProveedor for edit and NewContactoDeProveedorFormGroupInput for create.
 */
type ContactoDeProveedorFormGroupInput = IContactoDeProveedor | PartialWithRequiredKeyOf<NewContactoDeProveedor>;

type ContactoDeProveedorFormDefaults = Pick<NewContactoDeProveedor, 'id' | 'activo'>;

type ContactoDeProveedorFormGroupContent = {
  id: FormControl<IContactoDeProveedor['id'] | NewContactoDeProveedor['id']>;
  nombre: FormControl<IContactoDeProveedor['nombre']>;
  email: FormControl<IContactoDeProveedor['email']>;
  telefono: FormControl<IContactoDeProveedor['telefono']>;
  rol: FormControl<IContactoDeProveedor['rol']>;
  activo: FormControl<IContactoDeProveedor['activo']>;
  proveedor: FormControl<IContactoDeProveedor['proveedor']>;
};

export type ContactoDeProveedorFormGroup = FormGroup<ContactoDeProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactoDeProveedorFormService {
  createContactoDeProveedorFormGroup(contactoDeProveedor?: ContactoDeProveedorFormGroupInput): ContactoDeProveedorFormGroup {
    const contactoDeProveedorRawValue = {
      ...this.getFormDefaults(),
      ...(contactoDeProveedor ?? { id: null }),
    };

    return new FormGroup<ContactoDeProveedorFormGroupContent>({
      id: new FormControl(
        { value: contactoDeProveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(contactoDeProveedorRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      email: new FormControl(contactoDeProveedorRawValue.email, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      telefono: new FormControl(contactoDeProveedorRawValue.telefono, {
        validators: [Validators.maxLength(40)],
      }),
      rol: new FormControl(contactoDeProveedorRawValue.rol, {
        validators: [Validators.maxLength(60)],
      }),
      activo: new FormControl(contactoDeProveedorRawValue.activo, {
        validators: [Validators.required],
      }),
      proveedor: new FormControl(contactoDeProveedorRawValue.proveedor, {
        validators: [Validators.required],
      }),
    });
  }

  getContactoDeProveedor(form: ContactoDeProveedorFormGroup): IContactoDeProveedor | NewContactoDeProveedor {
    return form.getRawValue();
  }

  resetForm(form: ContactoDeProveedorFormGroup, contactoDeProveedor: ContactoDeProveedorFormGroupInput): void {
    const contactoDeProveedorRawValue = { ...this.getFormDefaults(), ...contactoDeProveedor };
    form.reset({
      ...contactoDeProveedorRawValue,
      id: { value: contactoDeProveedorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContactoDeProveedorFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
