import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { IncidenteDeProveedorDetail } from './incidente-de-proveedor-detail';

describe('IncidenteDeProveedor Management Detail Component', () => {
  let comp: IncidenteDeProveedorDetail;
  let fixture: ComponentFixture<IncidenteDeProveedorDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./incidente-de-proveedor-detail').then(m => m.IncidenteDeProveedorDetail),
              resolve: { incidenteDeProveedor: () => of({ id: 12957 }) },
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
    fixture = TestBed.createComponent(IncidenteDeProveedorDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load incidenteDeProveedor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IncidenteDeProveedorDetail);

      // THEN
      expect(instance.incidenteDeProveedor()).toEqual(expect.objectContaining({ id: 12957 }));
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
