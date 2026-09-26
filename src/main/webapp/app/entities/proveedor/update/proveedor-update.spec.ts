import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

import { ProveedorFormService } from './proveedor-form.service';
import { ProveedorUpdate } from './proveedor-update';

describe('Proveedor Management Update Component', () => {
  let comp: ProveedorUpdate;
  let fixture: ComponentFixture<ProveedorUpdate>;
  let activatedRoute: ActivatedRoute;
  let proveedorFormService: ProveedorFormService;
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

    fixture = TestBed.createComponent(ProveedorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proveedorFormService = TestBed.inject(ProveedorFormService);
    proveedorService = TestBed.inject(ProveedorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const proveedor: IProveedor = { id: 23574 };

      activatedRoute.data = of({ proveedor });
      comp.ngOnInit();

      expect(comp.proveedor).toEqual(proveedor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProveedor>();
      const proveedor = { id: 9668 };
      vitest.spyOn(proveedorFormService, 'getProveedor').mockReturnValue(proveedor);
      vitest.spyOn(proveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(proveedor);
      saveSubject.complete();

      // THEN
      expect(proveedorFormService.getProveedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proveedorService.update).toHaveBeenCalledWith(expect.objectContaining(proveedor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProveedor>();
      const proveedor = { id: 9668 };
      vitest.spyOn(proveedorFormService, 'getProveedor').mockReturnValue({ id: null });
      vitest.spyOn(proveedorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proveedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(proveedor);
      saveSubject.complete();

      // THEN
      expect(proveedorFormService.getProveedor).toHaveBeenCalled();
      expect(proveedorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProveedor>();
      const proveedor = { id: 9668 };
      vitest.spyOn(proveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proveedorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
