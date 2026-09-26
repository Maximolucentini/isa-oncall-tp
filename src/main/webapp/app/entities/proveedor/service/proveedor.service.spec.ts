import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProveedor } from '../proveedor.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../proveedor.test-samples';

import { ProveedorService } from './proveedor.service';

const requireRestSample: IProveedor = {
  ...sampleWithRequiredData,
};

describe('Proveedor Service', () => {
  let service: ProveedorService;
  let httpMock: HttpTestingController;
  let expectedResult: IProveedor | IProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProveedorService);
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

    it('should create a Proveedor', () => {
      const proveedor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(proveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Proveedor', () => {
      const proveedor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(proveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Proveedor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Proveedor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Proveedor', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProveedorToCollectionIfMissing', () => {
      it('should add a Proveedor to an empty array', () => {
        const proveedor: IProveedor = sampleWithRequiredData;
        expectedResult = service.addProveedorToCollectionIfMissing([], proveedor);
        expect(expectedResult).toEqual([proveedor]);
      });

      it('should not add a Proveedor to an array that contains it', () => {
        const proveedor: IProveedor = sampleWithRequiredData;
        const proveedorCollection: IProveedor[] = [
          {
            ...proveedor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, proveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Proveedor to an array that doesn't contain it", () => {
        const proveedor: IProveedor = sampleWithRequiredData;
        const proveedorCollection: IProveedor[] = [sampleWithPartialData];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, proveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proveedor);
      });

      it('should add only unique Proveedor to an array', () => {
        const proveedorArray: IProveedor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const proveedorCollection: IProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, ...proveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proveedor: IProveedor = sampleWithRequiredData;
        const proveedor2: IProveedor = sampleWithPartialData;
        expectedResult = service.addProveedorToCollectionIfMissing([], proveedor, proveedor2);
        expect(expectedResult).toEqual([proveedor, proveedor2]);
      });

      it('should accept null and undefined values', () => {
        const proveedor: IProveedor = sampleWithRequiredData;
        expectedResult = service.addProveedorToCollectionIfMissing([], null, proveedor, undefined);
        expect(expectedResult).toEqual([proveedor]);
      });

      it('should return initial array if no Proveedor is added', () => {
        const proveedorCollection: IProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, undefined, null);
        expect(expectedResult).toEqual(proveedorCollection);
      });
    });

    describe('compareProveedor', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProveedor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9668 };
        const entity2 = null;

        const compareResult1 = service.compareProveedor(entity1, entity2);
        const compareResult2 = service.compareProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9668 };
        const entity2 = { id: 23574 };

        const compareResult1 = service.compareProveedor(entity1, entity2);
        const compareResult2 = service.compareProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9668 };
        const entity2 = { id: 9668 };

        const compareResult1 = service.compareProveedor(entity1, entity2);
        const compareResult2 = service.compareProveedor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
