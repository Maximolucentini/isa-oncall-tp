import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ContactoDeProveedorDetail } from './contacto-de-proveedor-detail';

describe('ContactoDeProveedor Management Detail Component', () => {
  let comp: ContactoDeProveedorDetail;
  let fixture: ComponentFixture<ContactoDeProveedorDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./contacto-de-proveedor-detail').then(m => m.ContactoDeProveedorDetail),
              resolve: { contactoDeProveedor: () => of({ id: 16585 }) },
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
    fixture = TestBed.createComponent(ContactoDeProveedorDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load contactoDeProveedor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContactoDeProveedorDetail);

      // THEN
      expect(instance.contactoDeProveedor()).toEqual(expect.objectContaining({ id: 16585 }));
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
