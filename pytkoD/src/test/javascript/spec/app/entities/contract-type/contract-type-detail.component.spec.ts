import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrosoftTestModule } from '../../../test.module';
import { ContractTypeDetailComponent } from 'app/entities/contract-type/contract-type-detail.component';
import { ContractType } from 'app/shared/model/contract-type.model';

describe('Component Tests', () => {
  describe('ContractType Management Detail Component', () => {
    let comp: ContractTypeDetailComponent;
    let fixture: ComponentFixture<ContractTypeDetailComponent>;
    const route = ({ data: of({ contractType: new ContractType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrosoftTestModule],
        declarations: [ContractTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContractTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContractTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contractType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contractType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
