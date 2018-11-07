/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDoneUpdateComponent } from 'app/entities/purchase-done/purchase-done-update.component';
import { PurchaseDoneService } from 'app/entities/purchase-done/purchase-done.service';
import { PurchaseDone } from 'app/shared/model/purchase-done.model';

describe('Component Tests', () => {
    describe('PurchaseDone Management Update Component', () => {
        let comp: PurchaseDoneUpdateComponent;
        let fixture: ComponentFixture<PurchaseDoneUpdateComponent>;
        let service: PurchaseDoneService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDoneUpdateComponent]
            })
                .overrideTemplate(PurchaseDoneUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchaseDoneUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseDoneService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PurchaseDone(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchaseDone = entity;
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
                    const entity = new PurchaseDone();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchaseDone = entity;
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
