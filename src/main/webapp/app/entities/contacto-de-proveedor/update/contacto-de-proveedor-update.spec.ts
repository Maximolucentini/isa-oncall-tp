import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { IContactoDeProveedor } from '../contacto-de-proveedor.model';
import { ContactoDeProveedorService } from '../service/contacto-de-proveedor.service';

import { ContactoDeProveedorFormService } from './contacto-de-proveedor-form.service';
import { ContactoDeProveedorUpdate } from './contacto-de-proveedor-update';

describe('ContactoDeProveedor Management Update Component', () => {
  let comp: ContactoDeProveedorUpdate;
  let fixture: ComponentFixture<ContactoDeProveedorUpdate>;
  let activatedRoute: ActivatedRoute;
  let contactoDeProveedorFormService: ContactoDeProveedorFormService;
  let contactoDeProveedorService: ContactoDeProveedorService;
  let proveedorService: ProveedorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ContactoDeProveedorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactoDeProveedorFormService = TestBed.inject(ContactoDeProveedorFormService);
    contactoDeProveedorService = TestBed.inject(ContactoDeProveedorService);
    proveedorService = TestBed.inject(ProveedorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Proveedor query and add missing value', () => {
      const contactoDeProveedor: IContactoDeProveedor = { id: 21551 };
      const proveedor: IProveedor = { id: 9668 };
      contactoDeProveedor.proveedor = proveedor;

      const proveedorCollection: IProveedor[] = [{ id: 9668 }];
      vitest.spyOn(proveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: proveedorCollection })));
      const additionalProveedors = [proveedor];
      const expectedCollection: IProveedor[] = [...additionalProveedors, ...proveedorCollection];
      vitest.spyOn(proveedorService, 'addProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contactoDeProveedor });
      comp.ngOnInit();

      expect(proveedorService.query).toHaveBeenCalled();
      expect(proveedorService.addProveedorToCollectionIfMissing).toHaveBeenCalledWith(
        proveedorCollection,
        ...additionalProveedors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.proveedorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const contactoDeProveedor: IContactoDeProveedor = { id: 21551 };
      const proveedor: IProveedor = { id: 9668 };
      contactoDeProveedor.proveedor = proveedor;

      activatedRoute.data = of({ contactoDeProveedor });
      comp.ngOnInit();

      expect(comp.proveedorsSharedCollection()).toContainEqual(proveedor);
      expect(comp.contactoDeProveedor).toEqual(contactoDeProveedor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactoDeProveedor>();
      const contactoDeProveedor = { id: 16585 };
      vitest.spyOn(contactoDeProveedorFormService, 'getContactoDeProveedor').mockReturnValue(contactoDeProveedor);
      vitest.spyOn(contactoDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactoDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactoDeProveedor);
      saveSubject.complete();

      // THEN
      expect(contactoDeProveedorFormService.getContactoDeProveedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactoDeProveedorService.update).toHaveBeenCalledWith(expect.objectContaining(contactoDeProveedor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactoDeProveedor>();
      const contactoDeProveedor = { id: 16585 };
      vitest.spyOn(contactoDeProveedorFormService, 'getContactoDeProveedor').mockReturnValue({ id: null });
      vitest.spyOn(contactoDeProveedorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactoDeProveedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactoDeProveedor);
      saveSubject.complete();

      // THEN
      expect(contactoDeProveedorFormService.getContactoDeProveedor).toHaveBeenCalled();
      expect(contactoDeProveedorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContactoDeProveedor>();
      const contactoDeProveedor = { id: 16585 };
      vitest.spyOn(contactoDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactoDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactoDeProveedorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProveedor', () => {
      it('should forward to proveedorService', () => {
        const entity = { id: 9668 };
        const entity2 = { id: 23574 };
        vitest.spyOn(proveedorService, 'compareProveedor');
        comp.compareProveedor(entity, entity2);
        expect(proveedorService.compareProveedor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
