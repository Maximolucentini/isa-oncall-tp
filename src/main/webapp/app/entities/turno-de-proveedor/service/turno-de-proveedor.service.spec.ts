import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITurnoDeProveedor } from '../turno-de-proveedor.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../turno-de-proveedor.test-samples';

import { RestTurnoDeProveedor, TurnoDeProveedorService } from './turno-de-proveedor.service';

const requireRestSample: RestTurnoDeProveedor = {
  ...sampleWithRequiredData,
  desde: sampleWithRequiredData.desde?.toJSON(),
  hasta: sampleWithRequiredData.hasta?.toJSON(),
};

describe('TurnoDeProveedor Service', () => {
  let service: TurnoDeProveedorService;
  let httpMock: HttpTestingController;
  let expectedResult: ITurnoDeProveedor | ITurnoDeProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TurnoDeProveedorService);
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

    it('should create a TurnoDeProveedor', () => {
      const turnoDeProveedor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(turnoDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TurnoDeProveedor', () => {
      const turnoDeProveedor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(turnoDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TurnoDeProveedor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TurnoDeProveedor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TurnoDeProveedor', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTurnoDeProveedorToCollectionIfMissing', () => {
      it('should add a TurnoDeProveedor to an empty array', () => {
        const turnoDeProveedor: ITurnoDeProveedor = sampleWithRequiredData;
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing([], turnoDeProveedor);
        expect(expectedResult).toEqual([turnoDeProveedor]);
      });

      it('should not add a TurnoDeProveedor to an array that contains it', () => {
        const turnoDeProveedor: ITurnoDeProveedor = sampleWithRequiredData;
        const turnoDeProveedorCollection: ITurnoDeProveedor[] = [
          {
            ...turnoDeProveedor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing(turnoDeProveedorCollection, turnoDeProveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TurnoDeProveedor to an array that doesn't contain it", () => {
        const turnoDeProveedor: ITurnoDeProveedor = sampleWithRequiredData;
        const turnoDeProveedorCollection: ITurnoDeProveedor[] = [sampleWithPartialData];
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing(turnoDeProveedorCollection, turnoDeProveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(turnoDeProveedor);
      });

      it('should add only unique TurnoDeProveedor to an array', () => {
        const turnoDeProveedorArray: ITurnoDeProveedor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const turnoDeProveedorCollection: ITurnoDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing(turnoDeProveedorCollection, ...turnoDeProveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const turnoDeProveedor: ITurnoDeProveedor = sampleWithRequiredData;
        const turnoDeProveedor2: ITurnoDeProveedor = sampleWithPartialData;
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing([], turnoDeProveedor, turnoDeProveedor2);
        expect(expectedResult).toEqual([turnoDeProveedor, turnoDeProveedor2]);
      });

      it('should accept null and undefined values', () => {
        const turnoDeProveedor: ITurnoDeProveedor = sampleWithRequiredData;
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing([], null, turnoDeProveedor, undefined);
        expect(expectedResult).toEqual([turnoDeProveedor]);
      });

      it('should return initial array if no TurnoDeProveedor is added', () => {
        const turnoDeProveedorCollection: ITurnoDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addTurnoDeProveedorToCollectionIfMissing(turnoDeProveedorCollection, undefined, null);
        expect(expectedResult).toEqual(turnoDeProveedorCollection);
      });
    });

    describe('compareTurnoDeProveedor', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTurnoDeProveedor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31113 };
        const entity2 = null;

        const compareResult1 = service.compareTurnoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareTurnoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31113 };
        const entity2 = { id: 18439 };

        const compareResult1 = service.compareTurnoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareTurnoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31113 };
        const entity2 = { id: 31113 };

        const compareResult1 = service.compareTurnoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareTurnoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
