/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseUpdateComponent } from 'app/entities/purchase/purchase-update.component';
import { PurchaseService } from 'app/entities/purchase/purchase.service';
import { Purchase } from 'app/shared/model/purchase.model';

describe('Component Tests', () => {
    describe('Purchase Management Update Component', () => {
        let comp: PurchaseUpdateComponent;
        let fixture: ComponentFixture<PurchaseUpdateComponent>;
        let service: PurchaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseUpdateComponent]
            })
                .overrideTemplate(PurchaseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchaseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Purchase(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchase = entity;
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
                    const entity = new Purchase();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchase = entity;
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
