import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrosoftTestModule } from '../../../test.module';
import { TechnologyUpdateComponent } from 'app/entities/technology/technology-update.component';
import { TechnologyService } from 'app/entities/technology/technology.service';
import { Technology } from 'app/shared/model/technology.model';

describe('Component Tests', () => {
  describe('Technology Management Update Component', () => {
    let comp: TechnologyUpdateComponent;
    let fixture: ComponentFixture<TechnologyUpdateComponent>;
    let service: TechnologyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrosoftTestModule],
        declarations: [TechnologyUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TechnologyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TechnologyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TechnologyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Technology(123);
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
        const entity = new Technology();
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
