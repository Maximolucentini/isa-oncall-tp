import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ContactoDeProveedor e2e test', () => {
  const contactoDeProveedorPageUrl = '/contacto-de-proveedor';
  let username: string;
  let password: string;
  const contactoDeProveedorSample = { nombre: 'lashes', email: 'Amalia.PuenteCuellar@yahoo.com', activo: true };

  let contactoDeProveedor;
  let proveedor;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/proveedors',
      body: {
        nombre: 'after instead',
        tipo: 'NUBE',
        zonaHoraria: 'question',
        telefonoContacto: 'bashfully cake boo',
        emailContacto: 'instead',
        urlSoporte: 'absent',
        urlEstado: 'comfortable',
        cobertura: 'VEINTICUATRO_SIETE',
        slaRespuestaMinutos: 4835,
        activo: false,
      },
    }).then(({ body }) => {
      proveedor = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contacto-de-proveedors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contacto-de-proveedors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contacto-de-proveedors/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/proveedors', {
      statusCode: 200,
      body: [proveedor],
    });

    cy.intercept('GET', '/api/turno-de-proveedors', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (contactoDeProveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contacto-de-proveedors/${contactoDeProveedor.id}`,
      }).then(() => {
        contactoDeProveedor = undefined;
      });
    }
  });

  afterEach(() => {
    if (proveedor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/proveedors/${proveedor.id}`,
      }).then(() => {
        proveedor = undefined;
      });
    }
  });

  it('ContactoDeProveedors menu should load ContactoDeProveedors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contacto-de-proveedor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactoDeProveedor').should('exist');
    cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
  });

  describe('ContactoDeProveedor page', () => {
    it('should have translated page title', () => {
      cy.visit(contactoDeProveedorPageUrl);
      cy.getEntityHeading('ContactoDeProveedor').should('not.contain', 'oncallApp.contactoDeProveedor.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contactoDeProveedorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContactoDeProveedor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contactoDeProveedorPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ContactoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contacto-de-proveedors',
          body: {
            ...contactoDeProveedorSample,
            proveedor,
          },
        }).then(({ body }) => {
          contactoDeProveedor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contacto-de-proveedors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contactoDeProveedor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contactoDeProveedorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContactoDeProveedor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contactoDeProveedor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
      });

      it('edit button click should load edit ContactoDeProveedor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
      });

      it('edit button click should load edit ContactoDeProveedor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactoDeProveedor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
      });

      it('last delete button click should delete instance of ContactoDeProveedor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contactoDeProveedor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactoDeProveedorPageUrl);

        contactoDeProveedor = undefined;
      });
    });
  });

  describe('new ContactoDeProveedor page', () => {
    beforeEach(() => {
      cy.visit(contactoDeProveedorPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContactoDeProveedor');
    });

    it('should create an instance of ContactoDeProveedor', () => {
      cy.get(`[data-cy="nombre"]`).type('outside glum yowza');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'outside glum yowza');

      cy.get(`[data-cy="email"]`).type('Benjamin.CaballeroMondragon@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Benjamin.CaballeroMondragon@gmail.com');

      cy.get(`[data-cy="telefono"]`).type('long');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'long');

      cy.get(`[data-cy="rol"]`).type('whenever morning busily');
      cy.get(`[data-cy="rol"]`).should('have.value', 'whenever morning busily');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(`[data-cy="proveedor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contactoDeProveedor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contactoDeProveedorPageUrl);
    });
  });
});
