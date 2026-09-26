import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContactoDeProveedor } from '../contacto-de-proveedor.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../contacto-de-proveedor.test-samples';

import { ContactoDeProveedorService } from './contacto-de-proveedor.service';

const requireRestSample: IContactoDeProveedor = {
  ...sampleWithRequiredData,
};

describe('ContactoDeProveedor Service', () => {
  let service: ContactoDeProveedorService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactoDeProveedor | IContactoDeProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContactoDeProveedorService);
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

    it('should create a ContactoDeProveedor', () => {
      const contactoDeProveedor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactoDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactoDeProveedor', () => {
      const contactoDeProveedor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactoDeProveedor).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactoDeProveedor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactoDeProveedor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactoDeProveedor', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContactoDeProveedorToCollectionIfMissing', () => {
      it('should add a ContactoDeProveedor to an empty array', () => {
        const contactoDeProveedor: IContactoDeProveedor = sampleWithRequiredData;
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing([], contactoDeProveedor);
        expect(expectedResult).toEqual([contactoDeProveedor]);
      });

      it('should not add a ContactoDeProveedor to an array that contains it', () => {
        const contactoDeProveedor: IContactoDeProveedor = sampleWithRequiredData;
        const contactoDeProveedorCollection: IContactoDeProveedor[] = [
          {
            ...contactoDeProveedor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing(contactoDeProveedorCollection, contactoDeProveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactoDeProveedor to an array that doesn't contain it", () => {
        const contactoDeProveedor: IContactoDeProveedor = sampleWithRequiredData;
        const contactoDeProveedorCollection: IContactoDeProveedor[] = [sampleWithPartialData];
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing(contactoDeProveedorCollection, contactoDeProveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactoDeProveedor);
      });

      it('should add only unique ContactoDeProveedor to an array', () => {
        const contactoDeProveedorArray: IContactoDeProveedor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactoDeProveedorCollection: IContactoDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing(contactoDeProveedorCollection, ...contactoDeProveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactoDeProveedor: IContactoDeProveedor = sampleWithRequiredData;
        const contactoDeProveedor2: IContactoDeProveedor = sampleWithPartialData;
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing([], contactoDeProveedor, contactoDeProveedor2);
        expect(expectedResult).toEqual([contactoDeProveedor, contactoDeProveedor2]);
      });

      it('should accept null and undefined values', () => {
        const contactoDeProveedor: IContactoDeProveedor = sampleWithRequiredData;
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing([], null, contactoDeProveedor, undefined);
        expect(expectedResult).toEqual([contactoDeProveedor]);
      });

      it('should return initial array if no ContactoDeProveedor is added', () => {
        const contactoDeProveedorCollection: IContactoDeProveedor[] = [sampleWithRequiredData];
        expectedResult = service.addContactoDeProveedorToCollectionIfMissing(contactoDeProveedorCollection, undefined, null);
        expect(expectedResult).toEqual(contactoDeProveedorCollection);
      });
    });

    describe('compareContactoDeProveedor', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactoDeProveedor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16585 };
        const entity2 = null;

        const compareResult1 = service.compareContactoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareContactoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16585 };
        const entity2 = { id: 21551 };

        const compareResult1 = service.compareContactoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareContactoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16585 };
        const entity2 = { id: 16585 };

        const compareResult1 = service.compareContactoDeProveedor(entity1, entity2);
        const compareResult2 = service.compareContactoDeProveedor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
