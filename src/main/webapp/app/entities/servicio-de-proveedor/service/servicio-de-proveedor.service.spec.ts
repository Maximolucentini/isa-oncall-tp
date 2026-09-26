import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IServicioDeProveedor } from '../servicio-de-proveedor.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../servicio-de-proveedor.test-samples';

import { ServicioDeProveedorService } from './servicio-de-proveedor.service';

const requireRestSample: IServicioDeProveedor = {
  ...sampleWithRequiredData,
};

describe('ServicioDeProveedor Service', () => {
  let service: ServicioDeProveedorService;
  let httpMock: HttpTestingController;
  let expectedResult: IServicioDeProveedor | IServicioDeProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServicioDeProveedorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ServicioDeProveedor', () => {
      const servicioDeProveedor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(servicioDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServicioDeProveedor', () => {
      const servicioDeProveedor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(servicioDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServicioDeProveedor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServicioDeProveedor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServicioDeProveedor', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addServicioDeProveedorToCollectionIfMissing', () => {
      it('should add a ServicioDeProveedor to an empty array', () => {
        const servicioDeProveedor: IServicioDeProveedor = sampleWithRequiredData;
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing([], servicioDeProveedor);
        expect(expectedResult).toEqual([servicioDeProveedor]);
      });

      it('should not add a ServicioDeProveedor to an array that contains it', () => {
        const servicioDeProveedor: IServicioDeProveedor = sampleWithRequiredData;
        const servicioDeProveedorCollection: IServicioDeProveedor[] = [
          {
            ...servicioDeProveedor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing(servicioDeProveedorCollection, servicioDeProveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServicioDeProveedor to an array that doesn't contain it", () => {
        const servicioDeProveedor: IServicioDeProveedor = sampleWithRequiredData;
        const servicioDeProveedorCollection: IServicioDeProveedor[] = [sampleWithPartialData];
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing(servicioDeProveedorCollection, servicioDeProveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(servicioDeProveedor);
      });

      it('should add only unique ServicioDeProveedor to an array', () => {
        const servicioDeProveedorArray: IServicioDeProveedor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const servicioDeProveedorCollection: IServicioDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing(servicioDeProveedorCollection, ...servicioDeProveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const servicioDeProveedor: IServicioDeProveedor = sampleWithRequiredData;
        const servicioDeProveedor2: IServicioDeProveedor = sampleWithPartialData;
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing([], servicioDeProveedor, servicioDeProveedor2);
        expect(expectedResult).toEqual([servicioDeProveedor, servicioDeProveedor2]);
      });

      it('should accept null and undefined values', () => {
        const servicioDeProveedor: IServicioDeProveedor = sampleWithRequiredData;
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing([], null, servicioDeProveedor, undefined);
        expect(expectedResult).toEqual([servicioDeProveedor]);
      });

      it('should return initial array if no ServicioDeProveedor is added', () => {
        const servicioDeProveedorCollection: IServicioDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addServicioDeProveedorToCollectionIfMissing(servicioDeProveedorCollection, undefined, null);
        expect(expectedResult).toEqual(servicioDeProveedorCollection);
      });
    });

    describe('compareServicioDeProveedor', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServicioDeProveedor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6402 };
        const entity2 = null;

        const compareResult1 = service.compareServicioDeProveedor(entity1, entity2);
        const compareResult2 = service.compareServicioDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6402 };
        const entity2 = { id: 832 };

        const compareResult1 = service.compareServicioDeProveedor(entity1, entity2);
        const compareResult2 = service.compareServicioDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6402 };
        const entity2 = { id: 6402 };

        const compareResult1 = service.compareServicioDeProveedor(entity1, entity2);
        const compareResult2 = service.compareServicioDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
