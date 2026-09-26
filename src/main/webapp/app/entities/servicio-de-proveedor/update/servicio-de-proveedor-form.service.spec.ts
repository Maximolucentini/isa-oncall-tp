import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../servicio-de-proveedor.test-samples';

import { ServicioDeProveedorFormService } from './servicio-de-proveedor-form.service';

describe('ServicioDeProveedor Form Service', () => {
  let service: ServicioDeProveedorFormService;

  beforeEach(() => {
    service = TestBed.inject(ServicioDeProveedorFormService);
  });

  describe('Service methods', () => {
    describe('createServicioDeProveedorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServicioDeProveedorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            identificadorExterno: expect.any(Object),
            descripcion: expect.any(Object),
            slaRespuestaMinutos: expect.any(Object),
            activo: expect.any(Object),
            proveedor: expect.any(Object),
            servicioInternos: expect.any(Object),
          }),
        );
      });

      it('passing IServicioDeProveedor should create a new form with FormGroup', () => {
        const formGroup = service.createServicioDeProveedorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            identificadorExterno: expect.any(Object),
            descripcion: expect.any(Object),
            slaRespuestaMinutos: expect.any(Object),
            activo: expect.any(Object),
            proveedor: expect.any(Object),
            servicioInternos: expect.any(Object),
          }),
        );
      });
    });

    describe('getServicioDeProveedor', () => {
      it('should return NewServicioDeProveedor for default ServicioDeProveedor initial value', () => {
        const formGroup = service.createServicioDeProveedorFormGroup(sampleWithNewData);

        const servicioDeProveedor = service.getServicioDeProveedor(formGroup);

        expect(servicioDeProveedor).toMatchObject(sampleWithNewData);
      });

      it('should return NewServicioDeProveedor for empty ServicioDeProveedor initial value', () => {
        const formGroup = service.createServicioDeProveedorFormGroup();

        const servicioDeProveedor = service.getServicioDeProveedor(formGroup);

        expect(servicioDeProveedor).toMatchObject({});
      });

      it('should return IServicioDeProveedor', () => {
        const formGroup = service.createServicioDeProveedorFormGroup(sampleWithRequiredData);

        const servicioDeProveedor = service.getServicioDeProveedor(formGroup);

        expect(servicioDeProveedor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServicioDeProveedor should not enable id FormControl', () => {
        const formGroup = service.createServicioDeProveedorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServicioDeProveedor should disable id FormControl', () => {
        const formGroup = service.createServicioDeProveedorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
