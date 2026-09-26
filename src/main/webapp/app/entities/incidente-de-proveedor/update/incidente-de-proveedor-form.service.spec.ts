import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../incidente-de-proveedor.test-samples';

import { IncidenteDeProveedorFormService } from './incidente-de-proveedor-form.service';

describe('IncidenteDeProveedor Form Service', () => {
  let service: IncidenteDeProveedorFormService;

  beforeEach(() => {
    service = TestBed.inject(IncidenteDeProveedorFormService);
  });

  describe('Service methods', () => {
    describe('createIncidenteDeProveedorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ticketExterno: expect.any(Object),
            estado: expect.any(Object),
            abiertoEn: expect.any(Object),
            primeraRespuestaEn: expect.any(Object),
            resueltoEn: expect.any(Object),
            cumplioSla: expect.any(Object),
            motivoRechazo: expect.any(Object),
            responsableResuelto: expect.any(Object),
            nivelResuelto: expect.any(Object),
            huboCobertura: expect.any(Object),
            notas: expect.any(Object),
            incidente: expect.any(Object),
            servicioDeProveedor: expect.any(Object),
            abiertoPor: expect.any(Object),
          }),
        );
      });

      it('passing IIncidenteDeProveedor should create a new form with FormGroup', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ticketExterno: expect.any(Object),
            estado: expect.any(Object),
            abiertoEn: expect.any(Object),
            primeraRespuestaEn: expect.any(Object),
            resueltoEn: expect.any(Object),
            cumplioSla: expect.any(Object),
            motivoRechazo: expect.any(Object),
            responsableResuelto: expect.any(Object),
            nivelResuelto: expect.any(Object),
            huboCobertura: expect.any(Object),
            notas: expect.any(Object),
            incidente: expect.any(Object),
            servicioDeProveedor: expect.any(Object),
            abiertoPor: expect.any(Object),
          }),
        );
      });
    });

    describe('getIncidenteDeProveedor', () => {
      it('should return NewIncidenteDeProveedor for default IncidenteDeProveedor initial value', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup(sampleWithNewData);

        const incidenteDeProveedor = service.getIncidenteDeProveedor(formGroup);

        expect(incidenteDeProveedor).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncidenteDeProveedor for empty IncidenteDeProveedor initial value', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup();

        const incidenteDeProveedor = service.getIncidenteDeProveedor(formGroup);

        expect(incidenteDeProveedor).toMatchObject({});
      });

      it('should return IIncidenteDeProveedor', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup(sampleWithRequiredData);

        const incidenteDeProveedor = service.getIncidenteDeProveedor(formGroup);

        expect(incidenteDeProveedor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncidenteDeProveedor should not enable id FormControl', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncidenteDeProveedor should disable id FormControl', () => {
        const formGroup = service.createIncidenteDeProveedorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
