import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contacto-de-proveedor.test-samples';

import { ContactoDeProveedorFormService } from './contacto-de-proveedor-form.service';

describe('ContactoDeProveedor Form Service', () => {
  let service: ContactoDeProveedorFormService;

  beforeEach(() => {
    service = TestBed.inject(ContactoDeProveedorFormService);
  });

  describe('Service methods', () => {
    describe('createContactoDeProveedorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactoDeProveedorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            rol: expect.any(Object),
            activo: expect.any(Object),
            proveedor: expect.any(Object),
          }),
        );
      });

      it('passing IContactoDeProveedor should create a new form with FormGroup', () => {
        const formGroup = service.createContactoDeProveedorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            rol: expect.any(Object),
            activo: expect.any(Object),
            proveedor: expect.any(Object),
          }),
        );
      });
    });

    describe('getContactoDeProveedor', () => {
      it('should return NewContactoDeProveedor for default ContactoDeProveedor initial value', () => {
        const formGroup = service.createContactoDeProveedorFormGroup(sampleWithNewData);

        const contactoDeProveedor = service.getContactoDeProveedor(formGroup);

        expect(contactoDeProveedor).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactoDeProveedor for empty ContactoDeProveedor initial value', () => {
        const formGroup = service.createContactoDeProveedorFormGroup();

        const contactoDeProveedor = service.getContactoDeProveedor(formGroup);

        expect(contactoDeProveedor).toMatchObject({});
      });

      it('should return IContactoDeProveedor', () => {
        const formGroup = service.createContactoDeProveedorFormGroup(sampleWithRequiredData);

        const contactoDeProveedor = service.getContactoDeProveedor(formGroup);

        expect(contactoDeProveedor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactoDeProveedor should not enable id FormControl', () => {
        const formGroup = service.createContactoDeProveedorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactoDeProveedor should disable id FormControl', () => {
        const formGroup = service.createContactoDeProveedorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
