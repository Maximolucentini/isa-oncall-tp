import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IIncidenteDeProveedor } from '../incidente-de-proveedor.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../incidente-de-proveedor.test-samples';

import { IncidenteDeProveedorService, RestIncidenteDeProveedor } from './incidente-de-proveedor.service';

const requireRestSample: RestIncidenteDeProveedor = {
  ...sampleWithRequiredData,
  abiertoEn: sampleWithRequiredData.abiertoEn?.toJSON(),
  primeraRespuestaEn: sampleWithRequiredData.primeraRespuestaEn?.toJSON(),
  resueltoEn: sampleWithRequiredData.resueltoEn?.toJSON(),
};

describe('IncidenteDeProveedor Service', () => {
  let service: IncidenteDeProveedorService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncidenteDeProveedor | IIncidenteDeProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IncidenteDeProveedorService);
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

    it('should create a IncidenteDeProveedor', () => {
      const incidenteDeProveedor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incidenteDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncidenteDeProveedor', () => {
      const incidenteDeProveedor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incidenteDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncidenteDeProveedor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncidenteDeProveedor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncidenteDeProveedor', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addIncidenteDeProveedorToCollectionIfMissing', () => {
      it('should add a IncidenteDeProveedor to an empty array', () => {
        const incidenteDeProveedor: IIncidenteDeProveedor = sampleWithRequiredData;
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing([], incidenteDeProveedor);
        expect(expectedResult).toEqual([incidenteDeProveedor]);
      });

      it('should not add a IncidenteDeProveedor to an array that contains it', () => {
        const incidenteDeProveedor: IIncidenteDeProveedor = sampleWithRequiredData;
        const incidenteDeProveedorCollection: IIncidenteDeProveedor[] = [
          {
            ...incidenteDeProveedor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing(incidenteDeProveedorCollection, incidenteDeProveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncidenteDeProveedor to an array that doesn't contain it", () => {
        const incidenteDeProveedor: IIncidenteDeProveedor = sampleWithRequiredData;
        const incidenteDeProveedorCollection: IIncidenteDeProveedor[] = [sampleWithPartialData];
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing(incidenteDeProveedorCollection, incidenteDeProveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incidenteDeProveedor);
      });

      it('should add only unique IncidenteDeProveedor to an array', () => {
        const incidenteDeProveedorArray: IIncidenteDeProveedor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incidenteDeProveedorCollection: IIncidenteDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing(incidenteDeProveedorCollection, ...incidenteDeProveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incidenteDeProveedor: IIncidenteDeProveedor = sampleWithRequiredData;
        const incidenteDeProveedor2: IIncidenteDeProveedor = sampleWithPartialData;
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing([], incidenteDeProveedor, incidenteDeProveedor2);
        expect(expectedResult).toEqual([incidenteDeProveedor, incidenteDeProveedor2]);
      });

      it('should accept null and undefined values', () => {
        const incidenteDeProveedor: IIncidenteDeProveedor = sampleWithRequiredData;
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing([], null, incidenteDeProveedor, undefined);
        expect(expectedResult).toEqual([incidenteDeProveedor]);
      });

      it('should return initial array if no IncidenteDeProveedor is added', () => {
        const incidenteDeProveedorCollection: IIncidenteDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addIncidenteDeProveedorToCollectionIfMissing(incidenteDeProveedorCollection, undefined, null);
        expect(expectedResult).toEqual(incidenteDeProveedorCollection);
      });
    });

    describe('compareIncidenteDeProveedor', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncidenteDeProveedor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12957 };
        const entity2 = null;

        const compareResult1 = service.compareIncidenteDeProveedor(entity1, entity2);
        const compareResult2 = service.compareIncidenteDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12957 };
        const entity2 = { id: 29145 };

        const compareResult1 = service.compareIncidenteDeProveedor(entity1, entity2);
        const compareResult2 = service.compareIncidenteDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12957 };
        const entity2 = { id: 12957 };

        const compareResult1 = service.compareIncidenteDeProveedor(entity1, entity2);
        const compareResult2 = service.compareIncidenteDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
