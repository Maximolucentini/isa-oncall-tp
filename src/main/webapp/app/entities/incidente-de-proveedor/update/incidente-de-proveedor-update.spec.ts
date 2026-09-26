import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { ServicioDeProveedorService } from 'app/entities/servicio-de-proveedor/service/servicio-de-proveedor.service';
import { IServicioDeProveedor } from 'app/entities/servicio-de-proveedor/servicio-de-proveedor.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IIncidenteDeProveedor } from '../incidente-de-proveedor.model';
import { IncidenteDeProveedorService } from '../service/incidente-de-proveedor.service';

import { IncidenteDeProveedorFormService } from './incidente-de-proveedor-form.service';
import { IncidenteDeProveedorUpdate } from './incidente-de-proveedor-update';

describe('IncidenteDeProveedor Management Update Component', () => {
  let comp: IncidenteDeProveedorUpdate;
  let fixture: ComponentFixture<IncidenteDeProveedorUpdate>;
  let activatedRoute: ActivatedRoute;
  let incidenteDeProveedorFormService: IncidenteDeProveedorFormService;
  let incidenteDeProveedorService: IncidenteDeProveedorService;
  let incidenteService: IncidenteService;
  let servicioDeProveedorService: ServicioDeProveedorService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(IncidenteDeProveedorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incidenteDeProveedorFormService = TestBed.inject(IncidenteDeProveedorFormService);
    incidenteDeProveedorService = TestBed.inject(IncidenteDeProveedorService);
    incidenteService = TestBed.inject(IncidenteService);
    servicioDeProveedorService = TestBed.inject(ServicioDeProveedorService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Incidente query and add missing value', () => {
      const incidenteDeProveedor: IIncidenteDeProveedor = { id: 29145 };
      const incidente: IIncidente = { id: 31968 };
      incidenteDeProveedor.incidente = incidente;

      const incidenteCollection: IIncidente[] = [{ id: 31968 }];
      vitest.spyOn(incidenteService, 'query').mockReturnValue(of(new HttpResponse({ body: incidenteCollection })));
      const additionalIncidentes = [incidente];
      const expectedCollection: IIncidente[] = [...additionalIncidentes, ...incidenteCollection];
      vitest.spyOn(incidenteService, 'addIncidenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      expect(incidenteService.query).toHaveBeenCalled();
      expect(incidenteService.addIncidenteToCollectionIfMissing).toHaveBeenCalledWith(
        incidenteCollection,
        ...additionalIncidentes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.incidentesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ServicioDeProveedor query and add missing value', () => {
      const incidenteDeProveedor: IIncidenteDeProveedor = { id: 29145 };
      const servicioDeProveedor: IServicioDeProveedor = { id: 6402 };
      incidenteDeProveedor.servicioDeProveedor = servicioDeProveedor;

      const servicioDeProveedorCollection: IServicioDeProveedor[] = [{ id: 6402 }];
      vitest.spyOn(servicioDeProveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioDeProveedorCollection })));
      const additionalServicioDeProveedors = [servicioDeProveedor];
      const expectedCollection: IServicioDeProveedor[] = [...additionalServicioDeProveedors, ...servicioDeProveedorCollection];
      vitest.spyOn(servicioDeProveedorService, 'addServicioDeProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      expect(servicioDeProveedorService.query).toHaveBeenCalled();
      expect(servicioDeProveedorService.addServicioDeProveedorToCollectionIfMissing).toHaveBeenCalledWith(
        servicioDeProveedorCollection,
        ...additionalServicioDeProveedors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.servicioDeProveedorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const incidenteDeProveedor: IIncidenteDeProveedor = { id: 29145 };
      const abiertoPor: IUser = { id: 3944 };
      incidenteDeProveedor.abiertoPor = abiertoPor;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [abiertoPor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const incidenteDeProveedor: IIncidenteDeProveedor = { id: 29145 };
      const incidente: IIncidente = { id: 31968 };
      incidenteDeProveedor.incidente = incidente;
      const servicioDeProveedor: IServicioDeProveedor = { id: 6402 };
      incidenteDeProveedor.servicioDeProveedor = servicioDeProveedor;
      const abiertoPor: IUser = { id: 3944 };
      incidenteDeProveedor.abiertoPor = abiertoPor;

      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      expect(comp.incidentesSharedCollection()).toContainEqual(incidente);
      expect(comp.servicioDeProveedorsSharedCollection()).toContainEqual(servicioDeProveedor);
      expect(comp.usersSharedCollection()).toContainEqual(abiertoPor);
      expect(comp.incidenteDeProveedor).toEqual(incidenteDeProveedor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIncidenteDeProveedor>();
      const incidenteDeProveedor = { id: 12957 };
      vitest.spyOn(incidenteDeProveedorFormService, 'getIncidenteDeProveedor').mockReturnValue(incidenteDeProveedor);
      vitest.spyOn(incidenteDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(incidenteDeProveedor);
      saveSubject.complete();

      // THEN
      expect(incidenteDeProveedorFormService.getIncidenteDeProveedor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incidenteDeProveedorService.update).toHaveBeenCalledWith(expect.objectContaining(incidenteDeProveedor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIncidenteDeProveedor>();
      const incidenteDeProveedor = { id: 12957 };
      vitest.spyOn(incidenteDeProveedorFormService, 'getIncidenteDeProveedor').mockReturnValue({ id: null });
      vitest.spyOn(incidenteDeProveedorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidenteDeProveedor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(incidenteDeProveedor);
      saveSubject.complete();

      // THEN
      expect(incidenteDeProveedorFormService.getIncidenteDeProveedor).toHaveBeenCalled();
      expect(incidenteDeProveedorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IIncidenteDeProveedor>();
      const incidenteDeProveedor = { id: 12957 };
      vitest.spyOn(incidenteDeProveedorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidenteDeProveedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incidenteDeProveedorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIncidente', () => {
      it('should forward to incidenteService', () => {
        const entity = { id: 31968 };
        const entity2 = { id: 10195 };
        vitest.spyOn(incidenteService, 'compareIncidente');
        comp.compareIncidente(entity, entity2);
        expect(incidenteService.compareIncidente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareServicioDeProveedor', () => {
      it('should forward to servicioDeProveedorService', () => {
        const entity = { id: 6402 };
        const entity2 = { id: 832 };
        vitest.spyOn(servicioDeProveedorService, 'compareServicioDeProveedor');
        comp.compareServicioDeProveedor(entity, entity2);
        expect(servicioDeProveedorService.compareServicioDeProveedor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vitest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
