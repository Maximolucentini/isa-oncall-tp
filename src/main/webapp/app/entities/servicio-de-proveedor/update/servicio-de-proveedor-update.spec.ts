import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioDeProveedorService } from '../service/servicio-de-proveedor.service';
import { IServicioDeProveedor } from '../servicio-de-proveedor.model';

import { ServicioDeProveedorFormService } from './servicio-de-proveedor-form.service';
import { ServicioDeProveedorUpdate } from './servicio-de-proveedor-update';

describe('ServicioDeProveedor Management Update Component', () => {
  let comp: ServicioDeProveedorUpdate;
  let fixture: ComponentFixture<ServicioDeProveedorUpdate>;
  let activatedRoute: ActivatedRoute;
  let servicioDeProveedorFormService: ServicioDeProveedorFormService;
  let servicioDeProveedorService: ServicioDeProveedorService;
  let proveedorService: ProveedorService;
  let servicioService: ServicioService;

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

    fixture = TestBed.createComponent(ServicioDeProveedorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioDeProveedorFormService = TestBed.inject(ServicioDeProveedorFormService);
    servicioDeProveedorService = TestBed.inject(ServicioDeProveedorService);
    proveedorService = TestBed.inject(ProveedorService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Proveedor query and add missing value', () => {
      const servicioDeProveedor: IServicioDeProveedor = { id: 832 };
      const proveedor: IProveedor = { id: 9668 };
      servicioDeProveedor.proveedor = proveedor;

      const proveedorCollection: IProveedor[] = [{ id: 9668 }];
      vitest.spyOn(proveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: proveedorCollection })));
      const additionalProveedors = [proveedor];
      const expectedCollection: IProveedor[] = [...additionalProveedors, ...proveedorCollection];
      vitest.spyOn(proveedorService, 'addProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicioDeProveedor });
      comp.ngOnInit();

      expect(proveedorService.query).toHaveBeenCalled();
      expect(proveedorService.addProveedorToCollectionIfMissing).toHaveBeenCalledWith(
        proveedorCollection,
        ...additionalProveedors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.proveedorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Servicio query and add missing value', () => {
      const servicioDeProveedor: IServicioDeProveedor = { id: 832 };
      const servicioInternos: IServicio[] = [{ id: 24037 }];
      servicioDeProveedor.servicioInternos = servicioInternos;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [...servicioInternos];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicioDeProveedor });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const servicioDeProveedor: IServicioDeProveedor = { id: 832 };
      const proveedor: IProveedor = { id: 9668 };
      servicioDeProveedor.proveedor = proveedor;
      const servicioInterno: IServicio = { id: 24037 };
      servicioDeProveedor.servicioInternos = [servicioInterno];

      activatedRoute.data = of({ servicioDeProveedor });
      comp.ngOnInit();

      expect(comp.proveedorsSharedCollection()).toContainEqual(proveedor);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicioInterno);
      expect(comp.servicioDeProveedor).toEqual(servicioDeProveedor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioDeProveedor>();
      const servicioDeProveedor = { id: 6402 };
      vitest.spyOn(servicioDeProveedorFormService, 'getServicioDeProveedor').mockReturnValue(servicioDeProveedor);
      vitest.spyOn(servicioDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioDeProveedor);
      saveSubject.complete();

      // THEN
      expect(servicioDeProveedorFormService.getServicioDeProveedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioDeProveedorService.update).toHaveBeenCalledWith(expect.objectContaining(servicioDeProveedor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioDeProveedor>();
      const servicioDeProveedor = { id: 6402 };
      vitest.spyOn(servicioDeProveedorFormService, 'getServicioDeProveedor').mockReturnValue({ id: null });
      vitest.spyOn(servicioDeProveedorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioDeProveedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioDeProveedor);
      saveSubject.complete();

      // THEN
      expect(servicioDeProveedorFormService.getServicioDeProveedor).toHaveBeenCalled();
      expect(servicioDeProveedorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioDeProveedor>();
      const servicioDeProveedor = { id: 6402 };
      vitest.spyOn(servicioDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioDeProveedorService.update).toHaveBeenCalled();
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

    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
