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

describe('Proveedor e2e test', () => {
  const proveedorPageUrl = '/proveedor';
  let username: string;
  let password: string;
  const proveedorSample = { nombre: 'astride cool', tipo: 'OTRO', zonaHoraria: 'advocate', cobertura: 'HORARIO_HABIL', activo: true };

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
    cy.intercept('GET', '/api/proveedors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/proveedors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/proveedors/*').as('deleteEntityRequest');
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

  it('Proveedors menu should load Proveedors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('proveedor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Proveedor').should('exist');
    cy.location('pathname').should('eq', proveedorPageUrl);
  });

  describe('Proveedor page', () => {
    it('should have translated page title', () => {
      cy.visit(proveedorPageUrl);
      cy.getEntityHeading('Proveedor').should('not.contain', 'oncallApp.proveedor.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(proveedorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Proveedor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${proveedorPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Proveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', proveedorPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/proveedors',
          body: proveedorSample,
        }).then(({ body }) => {
          proveedor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/proveedors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/proveedors?page=0&size=20>; rel="last",<http://localhost/api/proveedors?page=0&size=20>; rel="first"',
              },
              body: [proveedor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(proveedorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Proveedor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('proveedor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', proveedorPageUrl);
      });

      it('edit button click should load edit Proveedor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Proveedor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', proveedorPageUrl);
      });

      it('edit button click should load edit Proveedor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Proveedor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', proveedorPageUrl);
      });

      it('last delete button click should delete instance of Proveedor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('proveedor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', proveedorPageUrl);

        proveedor = undefined;
      });
    });
  });

  describe('new Proveedor page', () => {
    beforeEach(() => {
      cy.visit(proveedorPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Proveedor');
    });

    it('should create an instance of Proveedor', () => {
      cy.get(`[data-cy="nombre"]`).type('deficient');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'deficient');

      cy.get(`[data-cy="tipo"]`).select('CDN');

      cy.get(`[data-cy="zonaHoraria"]`).type('however object sprinkles');
      cy.get(`[data-cy="zonaHoraria"]`).should('have.value', 'however object sprinkles');

      cy.get(`[data-cy="telefonoContacto"]`).type('eek');
      cy.get(`[data-cy="telefonoContacto"]`).should('have.value', 'eek');

      cy.get(`[data-cy="emailContacto"]`).type('towards arbitrate revitalise');
      cy.get(`[data-cy="emailContacto"]`).should('have.value', 'towards arbitrate revitalise');

      cy.get(`[data-cy="urlSoporte"]`).type('per');
      cy.get(`[data-cy="urlSoporte"]`).should('have.value', 'per');

      cy.get(`[data-cy="urlEstado"]`).type('symbolise towards despite');
      cy.get(`[data-cy="urlEstado"]`).should('have.value', 'symbolise towards despite');

      cy.get(`[data-cy="cobertura"]`).select('SOLO_CRITICO');

      cy.get(`[data-cy="slaRespuestaMinutos"]`).type('224');
      cy.get(`[data-cy="slaRespuestaMinutos"]`).should('have.value', '224');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        proveedor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', proveedorPageUrl);
    });
  });
});
