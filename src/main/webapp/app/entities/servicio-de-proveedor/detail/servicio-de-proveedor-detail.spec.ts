import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ServicioDeProveedorDetail } from './servicio-de-proveedor-detail';

describe('ServicioDeProveedor Management Detail Component', () => {
  let comp: ServicioDeProveedorDetail;
  let fixture: ComponentFixture<ServicioDeProveedorDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./servicio-de-proveedor-detail').then(m => m.ServicioDeProveedorDetail),
              resolve: { servicioDeProveedor: () => of({ id: 6402 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ServicioDeProveedorDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load servicioDeProveedor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServicioDeProveedorDetail);

      // THEN
      expect(instance.servicioDeProveedor()).toEqual(expect.objectContaining({ id: 6402 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
