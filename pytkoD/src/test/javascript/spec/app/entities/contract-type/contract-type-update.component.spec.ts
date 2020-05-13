import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrosoftTestModule } from '../../../test.module';
import { ContractTypeUpdateComponent } from 'app/entities/contract-type/contract-type-update.component';
import { ContractTypeService } from 'app/entities/contract-type/contract-type.service';
import { ContractType } from 'app/shared/model/contract-type.model';

describe('Component Tests', () => {
  describe('ContractType Management Update Component', () => {
    let comp: ContractTypeUpdateComponent;
    let fixture: ComponentFixture<ContractTypeUpdateComponent>;
    let service: ContractTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrosoftTestModule],
        declarations: [ContractTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContractTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContractTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContractTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContractType(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContractType();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
