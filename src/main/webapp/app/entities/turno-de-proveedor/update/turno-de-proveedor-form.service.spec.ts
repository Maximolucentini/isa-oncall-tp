import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../turno-de-proveedor.test-samples';

import { TurnoDeProveedorFormService } from './turno-de-proveedor-form.service';

describe('TurnoDeProveedor Form Service', () => {
  let service: TurnoDeProveedorFormService;

  beforeEach(() => {
    service = TestBed.inject(TurnoDeProveedorFormService);
  });

  describe('Service methods', () => {
    describe('createTurnoDeProveedorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            desde: expect.any(Object),
            hasta: expect.any(Object),
            nivel: expect.any(Object),
            esReemplazo: expect.any(Object),
            nota: expect.any(Object),
            contacto: expect.any(Object),
          }),
        );
      });

      it('passing ITurnoDeProveedor should create a new form with FormGroup', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            desde: expect.any(Object),
            hasta: expect.any(Object),
            nivel: expect.any(Object),
            esReemplazo: expect.any(Object),
            nota: expect.any(Object),
            contacto: expect.any(Object),
          }),
        );
      });
    });

    describe('getTurnoDeProveedor', () => {
      it('should return NewTurnoDeProveedor for default TurnoDeProveedor initial value', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup(sampleWithNewData);

        const turnoDeProveedor = service.getTurnoDeProveedor(formGroup);

        expect(turnoDeProveedor).toMatchObject(sampleWithNewData);
      });

      it('should return NewTurnoDeProveedor for empty TurnoDeProveedor initial value', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup();

        const turnoDeProveedor = service.getTurnoDeProveedor(formGroup);

        expect(turnoDeProveedor).toMatchObject({});
      });

      it('should return ITurnoDeProveedor', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup(sampleWithRequiredData);

        const turnoDeProveedor = service.getTurnoDeProveedor(formGroup);

        expect(turnoDeProveedor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITurnoDeProveedor should not enable id FormControl', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTurnoDeProveedor should disable id FormControl', () => {
        const formGroup = service.createTurnoDeProveedorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
