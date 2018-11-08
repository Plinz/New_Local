/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { HoldingUpdateComponent } from 'app/entities/holding/holding-update.component';
import { HoldingService } from 'app/entities/holding/holding.service';
import { Holding } from 'app/shared/model/holding.model';

describe('Component Tests', () => {
    describe('Holding Management Update Component', () => {
        let comp: HoldingUpdateComponent;
        let fixture: ComponentFixture<HoldingUpdateComponent>;
        let service: HoldingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [HoldingUpdateComponent]
            })
                .overrideTemplate(HoldingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HoldingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HoldingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Holding(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.holding = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Holding();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.holding = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
