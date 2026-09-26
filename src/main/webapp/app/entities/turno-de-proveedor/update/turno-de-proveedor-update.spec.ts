import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactoDeProveedor } from 'app/entities/contacto-de-proveedor/contacto-de-proveedor.model';
import { ContactoDeProveedorService } from 'app/entities/contacto-de-proveedor/service/contacto-de-proveedor.service';
import { TurnoDeProveedorService } from '../service/turno-de-proveedor.service';
import { ITurnoDeProveedor } from '../turno-de-proveedor.model';

import { TurnoDeProveedorFormService } from './turno-de-proveedor-form.service';
import { TurnoDeProveedorUpdate } from './turno-de-proveedor-update';

describe('TurnoDeProveedor Management Update Component', () => {
  let comp: TurnoDeProveedorUpdate;
  let fixture: ComponentFixture<TurnoDeProveedorUpdate>;
  let activatedRoute: ActivatedRoute;
  let turnoDeProveedorFormService: TurnoDeProveedorFormService;
  let turnoDeProveedorService: TurnoDeProveedorService;
  let contactoDeProveedorService: ContactoDeProveedorService;

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

    fixture = TestBed.createComponent(TurnoDeProveedorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    turnoDeProveedorFormService = TestBed.inject(TurnoDeProveedorFormService);
    turnoDeProveedorService = TestBed.inject(TurnoDeProveedorService);
    contactoDeProveedorService = TestBed.inject(ContactoDeProveedorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ContactoDeProveedor query and add missing value', () => {
      const turnoDeProveedor: ITurnoDeProveedor = { id: 18439 };
      const contacto: IContactoDeProveedor = { id: 16585 };
      turnoDeProveedor.contacto = contacto;

      const contactoDeProveedorCollection: IContactoDeProveedor[] = [{ id: 16585 }];
      vitest.spyOn(contactoDeProveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: contactoDeProveedorCollection })));
      const additionalContactoDeProveedors = [contacto];
      const expectedCollection: IContactoDeProveedor[] = [...additionalContactoDeProveedors, ...contactoDeProveedorCollection];
      vitest.spyOn(contactoDeProveedorService, 'addContactoDeProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ turnoDeProveedor });
      comp.ngOnInit();

      expect(contactoDeProveedorService.query).toHaveBeenCalled();
      expect(contactoDeProveedorService.addContactoDeProveedorToCollectionIfMissing).toHaveBeenCalledWith(
        contactoDeProveedorCollection,
        ...additionalContactoDeProveedors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactoDeProveedorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const turnoDeProveedor: ITurnoDeProveedor = { id: 18439 };
      const contacto: IContactoDeProveedor = { id: 16585 };
      turnoDeProveedor.contacto = contacto;

      activatedRoute.data = of({ turnoDeProveedor });
      comp.ngOnInit();

      expect(comp.contactoDeProveedorsSharedCollection()).toContainEqual(contacto);
      expect(comp.turnoDeProveedor).toEqual(turnoDeProveedor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITurnoDeProveedor>();
      const turnoDeProveedor = { id: 31113 };
      vitest.spyOn(turnoDeProveedorFormService, 'getTurnoDeProveedor').mockReturnValue(turnoDeProveedor);
      vitest.spyOn(turnoDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turnoDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(turnoDeProveedor);
      saveSubject.complete();

      // THEN
      expect(turnoDeProveedorFormService.getTurnoDeProveedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(turnoDeProveedorService.update).toHaveBeenCalledWith(expect.objectContaining(turnoDeProveedor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITurnoDeProveedor>();
      const turnoDeProveedor = { id: 31113 };
      vitest.spyOn(turnoDeProveedorFormService, 'getTurnoDeProveedor').mockReturnValue({ id: null });
      vitest.spyOn(turnoDeProveedorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turnoDeProveedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(turnoDeProveedor);
      saveSubject.complete();

      // THEN
      expect(turnoDeProveedorFormService.getTurnoDeProveedor).toHaveBeenCalled();
      expect(turnoDeProveedorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITurnoDeProveedor>();
      const turnoDeProveedor = { id: 31113 };
      vitest.spyOn(turnoDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turnoDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(turnoDeProveedorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContactoDeProveedor', () => {
      it('should forward to contactoDeProveedorService', () => {
        const entity = { id: 16585 };
        const entity2 = { id: 21551 };
        vitest.spyOn(contactoDeProveedorService, 'compareContactoDeProveedor');
        comp.compareContactoDeProveedor(entity, entity2);
        expect(contactoDeProveedorService.compareContactoDeProveedor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
