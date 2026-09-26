import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../proveedor.test-samples';

import { ProveedorFormService } from './proveedor-form.service';

describe('Proveedor Form Service', () => {
  let service: ProveedorFormService;

  beforeEach(() => {
    service = TestBed.inject(ProveedorFormService);
  });

  describe('Service methods', () => {
    describe('createProveedorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProveedorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            tipo: expect.any(Object),
            zonaHoraria: expect.any(Object),
            telefonoContacto: expect.any(Object),
            emailContacto: expect.any(Object),
            urlSoporte: expect.any(Object),
            urlEstado: expect.any(Object),
            cobertura: expect.any(Object),
            slaRespuestaMinutos: expect.any(Object),
            activo: expect.any(Object),
          }),
        );
      });

      it('passing IProveedor should create a new form with FormGroup', () => {
        const formGroup = service.createProveedorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            tipo: expect.any(Object),
            zonaHoraria: expect.any(Object),
            telefonoContacto: expect.any(Object),
            emailContacto: expect.any(Object),
            urlSoporte: expect.any(Object),
            urlEstado: expect.any(Object),
            cobertura: expect.any(Object),
            slaRespuestaMinutos: expect.any(Object),
            activo: expect.any(Object),
          }),
        );
      });
    });

    describe('getProveedor', () => {
      it('should return NewProveedor for default Proveedor initial value', () => {
        const formGroup = service.createProveedorFormGroup(sampleWithNewData);

        const proveedor = service.getProveedor(formGroup);

        expect(proveedor).toMatchObject(sampleWithNewData);
      });

      it('should return NewProveedor for empty Proveedor initial value', () => {
        const formGroup = service.createProveedorFormGroup();

        const proveedor = service.getProveedor(formGroup);

        expect(proveedor).toMatchObject({});
      });

      it('should return IProveedor', () => {
        const formGroup = service.createProveedorFormGroup(sampleWithRequiredData);

        const proveedor = service.getProveedor(formGroup);

        expect(proveedor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProveedor should not enable id FormControl', () => {
        const formGroup = service.createProveedorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProveedor should disable id FormControl', () => {
        const formGroup = service.createProveedorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
